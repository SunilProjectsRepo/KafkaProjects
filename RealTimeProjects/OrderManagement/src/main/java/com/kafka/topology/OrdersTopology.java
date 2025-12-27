package com.kafka.topology;

import com.kafka.domain.Order;
import com.kafka.domain.OrderType;
import com.kafka.domain.Revenue;
import com.kafka.serdes.SerdesFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

@Slf4j
public class OrdersTopology {
    public static final String ORDERS = "orders";
    public static final String GENERAL_ORDERS = "general_orders";

    public static final String RESTAURANT_ORDERS = "restaurant_orders";

    public static final String STORES = "stores";


    public static Topology buildTopology(){
        Predicate<String, Order> generalPredicates = (key, order) -> order.orderType().equals(OrderType.GENERAL);
        Predicate<String, Order> restaurantPredicates = (key, order) -> order.orderType().equals(OrderType.RESTAURANT);

        ValueMapper<Order, Revenue> revenueMapper = order -> new Revenue(order.locationId(), order.finalAmount());

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        var ordersStream = streamsBuilder.stream(ORDERS,
                Consumed.with(Serdes.String(), SerdesFactory.orderSerdes()));

        ordersStream.print(Printed.<String, Order>toSysOut().withLabel("orders"));

        //Branching strategy
        ordersStream.split(Named.as("general-restaurant-stream"))
            //predicate represents here as condition required to split the orders
            .branch(generalPredicates,
                Branched.withConsumer(generalOrderStream -> {
                    generalOrderStream
                            .print(Printed.<String, Order>toSysOut().withLabel("generalStream"));
                    generalOrderStream
                            //Transform the Order Domain Type to Revenue Domain Type
                            .mapValues(((readOnlyKey, value) -> revenueMapper.apply(value)))
                            .to(GENERAL_ORDERS,
                                    Produced.with(Serdes.String(), SerdesFactory.revenueSerdes()));
                }))
            .branch(restaurantPredicates,
                Branched.withConsumer(restaurantOrderStream -> {
                    restaurantOrderStream
                            .print(Printed.<String, Order>toSysOut().withLabel("restaurantStream"));
                    restaurantOrderStream
                            //Transform the Order Domain Type to Revenue Domain Type
                            .mapValues(((readOnlyKey, value) -> revenueMapper.apply(value)))
                            .to(RESTAURANT_ORDERS,
                                    Produced.with(Serdes.String(), SerdesFactory.revenueSerdes()));

                }));

        return streamsBuilder.build();
    }
}
