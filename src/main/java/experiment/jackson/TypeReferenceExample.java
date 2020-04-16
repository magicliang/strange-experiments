package experiment.jackson;

import lombok.Data;

/**
 * jackson 的typereference无法推导参数化类型，看起来能推导出来，取实际类型数据的时候，还是linkhashmap。
 */
public class TypeReferenceExample {

}

@Data
class A {
    private String a;

}

