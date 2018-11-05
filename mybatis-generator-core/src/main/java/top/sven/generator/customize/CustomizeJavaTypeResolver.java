package top.sven.generator.customize;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;

/**
 * @author liulang
 * @date 2018/10/15
 **/
public class CustomizeJavaTypeResolver extends JavaTypeResolverDefaultImpl {
    public CustomizeJavaTypeResolver() {
        super();
        typeMap.put(Types.BOOLEAN, new JdbcTypeInformation("BOOLEAN",
                new FullyQualifiedJavaType(Integer.class.getName())));
        typeMap.put(Types.BIT, new JdbcTypeInformation("BIT",
                new FullyQualifiedJavaType(Integer.class.getName())));

    }
}
