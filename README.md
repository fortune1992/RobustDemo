# hotFixALL
整理thinker,robust热修复使用方法和原理

还有一个andfix已经维护了，不去研究了直接给出大致的原理

新旧apk通过patch工具生成patch补丁包，补丁包中会标记有bug的方法，为这个方法添加一个注解。然后将补丁包下发到手机内存后，通过dexclassloader加载这个dex补丁包，获取类中标记了自定义注解的这个方法，在C++层对这个方法进行重新赋值。达到一个修复的效果


美团robus

使用https://blog.csdn.net/qq_15527709/article/details/116144676

原理https://blog.csdn.net/qq_15527709/article/details/116202841
