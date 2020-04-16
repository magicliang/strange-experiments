package experiment.stream;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collector 主要是创造其他容器的操作，可以是线性容器（Collection），也可以是树状容器（Map）。
 * Created by LC on 2017/7/1.
 */
public class CollectorTest {

    public static void main(String[] args) {

        Stream<Entity> entityStream = Stream.of(new Entity(1, "1"), new Entity(1, "2"), new Entity(2, "2"));
        // 注意看，这里既像 hbase 的数据结构（Map + List），也像 Map reduce 的 intermediate 结果。
        Map<Integer, List<Entity>> result = entityStream.collect(Collectors.groupingBy(x -> x.getId()));
        System.out.println(result);

        //grouping by 的结果的key就是 behavioral parameters 的结果，这里就是 true 或者 false。
        Map<Boolean, List<Integer>> collectGroup = Stream.of(1, 2, 3, 4)
                .collect(Collectors.groupingBy(it -> it > 3));
        System.out.println("collectGroup : " + collectGroup);


        // System.out.println(entityStream.parallel().unordered().collect(Collectors.groupingBy(entity -> entity.getId())));

        // Stream 是不能复用的。所以每次都要重新创建流（这也是一个麻烦之处）。
        entityStream = Stream.of(new Entity(1, "1"), new Entity(1, "2"), new Entity(2, "2"));
        // 注意看，这里既像 hbase 的数据结构（Map + List），也像 Map reduce 的 intermediate 结果。
        result = entityStream.parallel().unordered().collect(Collectors.groupingByConcurrent(entity -> entity.getId()));
        System.out.println(result);

        entityStream = Stream.of(new Entity(1, "1"), new Entity(1, "2"), new Entity(2, "2"));
        // 第一个函数生成 key，第二个函数把 value map 成其他值，再把其他值组成其他数据结构。第二个函数的两个参数都是必须的
        System.out.println(entityStream.collect(Collectors.groupingBy(Entity::getId, Collectors.mapping(Entity::getName, Collectors.toList()))));

        entityStream = Stream.of(new Entity(1, "1", 1), new Entity(1, "2", 2), new Entity(2, "2", 3));
        // 第一个函数生成 key，第二个函数把决定了 map 结果的数据结构
        Map<Integer, Set<Entity>> result2 = entityStream.collect(Collectors.groupingBy(Entity::getId, Collectors.toSet()));
        System.out.println(result2);

        entityStream = Stream.of(new Entity(1, "1", 1), new Entity(1, "2", 2), new Entity(2, "2", 3));
        // Collectors 也支持 reducing 操作。这是一个类似数据 aggregate 的操作了。对相同姓名的人的年龄进行求和
        Map<String, Integer> result3 = entityStream.collect(Collectors.groupingBy(Entity::getName, Collectors.reducing(0,//种子也是默认值。
                Entity::getAge, Integer::sum// 注意看 Integer 自带的求和函数。这最后一个函数，就是 aggregate 也就是 reduce 。
        )));
        System.out.println(result3);
        //map 是最适合用来做record表达的，所以可以表达聚合结果。它的value 可以是数据结构（类似hbase的记录），也可以是实际值，也就是 SQL aggregation 的结果


        entityStream = Stream.of(new Entity(1, "1", 1), new Entity(1, "2", 2), new Entity(2, "2", 3));
        //averagingInt 的返回值居然可以是 double，这样反向做类型推测太难了。
        Map<String, Double> result4 = entityStream.collect(Collectors.groupingBy(Entity::getName, Collectors.averagingInt(Entity::getAge)));
        System.out.println(result4);
    }
}

class Entity {

    private Integer id;
    private String name;
    private Integer age;

    public Entity() {
    }

    public Entity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Entity(Integer id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "{id: " + id + ", name:　" + name + "}";
    }
}
