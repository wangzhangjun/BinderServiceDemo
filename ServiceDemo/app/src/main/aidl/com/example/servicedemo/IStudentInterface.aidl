// IStudentInterface.aidl
package com.example.servicedemo;
import com.example.servicedemo.IChangeCallback;
import com.example.servicedemo.StudentInfo;

// Declare any non-default types here with import statements
    /**
    AIDL的实现
    AIDL的应用场景，一般情况下是有两个进程，一个进程提供方法，一个进程调用方法。

    我们习惯将提供方法的进程定义为Service端、将调用方法的进程定义为Client，就是我们常说的AIDL服务端和AIDL客户端。

    AIDL的数据传输支持类型有特殊要求，并非所有的数据类型都能像以往一样传递：

    支持数据类型如下:
    1. Java 的原生类型
    2. String 和CharSequence
    3. List 和 Map ,List和Map 对象的元素必须是AIDL支持的数据类型；  以上三种类型都不需要导入(import)
    4. AIDL 自动生成的接口  需要导入(import)
    5. 实现android.os.Parcelable 接口的类.  需要导入(import)。
    那我们接下来演示，如何提供AIDL的服务端和客户端。

    这里重点是in、out、inout修饰符以及Parcelable的使用！常见的是in、Parcelable，少用的out、inout。

    这几种修饰符，可理解如下：

    in：客户端的参数输入；

    out：服务端的参数输入；

    inout：这个可以叫输入输出参数，客户端可输入、服务端也可输入。客户端输入了参数到服务端后，服务端也可对该参数进行修改等，最后在客户端上得到的是服务端输出的参数。
     */
interface IStudentInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);
    int getStudentId(String name);
    void setCallback(IChangeCallback callback);
    String getConvertName(in StudentInfo info);
    void getServiceStudentInfo(out StudentInfo serviceInfo);
    void getServiceStudentInfoInOut(inout StudentInfo serviceInfo);
}
