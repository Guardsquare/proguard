package proguard.optimize.kotlin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proguard.classfile.ClassPool;
import proguard.classfile.Clazz;
import proguard.classfile.ProgramClass;
import proguard.classfile.util.ClassUtil;
import proguard.classfile.visitor.ClassPoolVisitor;
import proguard.classfile.visitor.ClassVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * This class
 */
public class PackageGrouper implements ClassVisitor {

    private final Map<String, ClassPool> packageClassPools = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(PackageGrouper.class);

    @Override
    public void visitAnyClass(Clazz clazz)
    {
        String classPackageName = ClassUtil.internalPackageName(clazz.getName());
        // or
        // String classPackageName = ClassUtil.internalPackageName(clazz.getName());
        if (!packageClassPools.containsKey(classPackageName))
        {
            logger.info("New package found: {}", classPackageName);
            packageClassPools.put(classPackageName, new ClassPool());
        }
        packageClassPools.get(classPackageName).addClass(clazz);
    }

    public int size()
    {
        return packageClassPools.size();
    }

    public boolean containsPackage(String packageName)
    {
        return packageClassPools.containsKey(packageName);
    }

    public Iterable<String> packageNames()
    {
        return packageClassPools.keySet();
    }

    public void packagesAccept(ClassPoolVisitor classPoolVisitor)
    {
        for (ClassPool packageClassPool : packageClassPools.values())
        {
            classPoolVisitor.visitClassPool(packageClassPool);
        }
    }
}
