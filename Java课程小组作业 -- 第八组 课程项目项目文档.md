# Java课程小组作业 -- 第八组 课程项目项目文档

项目简介



## 第一次作业 deadline -- 12/09

- 01 实现文件夹的 Key-Value 生成，同时支持
  - 给定value，向存储中添加对应的key-value
  - 给定key，查找得到对应的value值
- 02 将文件夹转化为key, value 进行持久化存储

------

**实现思路：**

运用面向对象的方法，根据文件/文件夹创建类来进行抽象出概括的类。

创建一个接口用于类调用不同的计算哈希值的方法

首先创建一个抽象类用来作为父类，抽象类中定义抽象方法以供后面的子类进行实现

**创建一个文件类**

- 属性 private

  - 文件对象
  - 文件的哈希值
  - 文件的名称
  - 文件的文件类型 Blob / Tree

- 构造

  - 构造函数（传入文件的路径）
  - 构造函数（传入文件对象）

- 方法 public

  - 复制文件

  - get / set 方法

    

**创建一个文件夹类**

- 属性 private
  - 文件对象
  - 文件夹的哈希值
  - 文件夹的名称
  - 文件的文件类型 Blob / Tree
  - 文件夹要生成的文件的文件内容 `StringBuilder`
  - 文件夹中的文件对应的对象

- 构造
  - 构造函数（传入文件夹的路径）
  - 构造函数（传入文件夹对象）
- 方法
  - 创建文件夹对应的记录文件
  - get / set 方法
  - `toString` 方法



**创建一个包含工具方法的接口（暂定一个接口）**

方法 

- 复制文件生成新文件
- 计算哈希值
- 向文件中写入内容



创建一个类存放项目的要求实现方法

方法

- 查询
---

## 第二次作业 deadline -- 12/16
   -  实现commit功能

**实现思路**
   - 01 commit对象（key-value的形式存储）
     - key：commit对象的哈希值
     - value： 1.提交项目的根目录**tree的key** 2.前驱**commit对象的key**（从head文件中取得）3.代码author 4.代码committer 5.commit备注 6.commit时间戳
     - 创建一份文件，文件的内容是以上的value，根据这些value生成commit的key，作为文件的文件名
   - 02 HEAD指针
     - 建立一个文件 里面存储最新的commit的key
   - 03 commit过程
     - （新生成的commit，需要将提交的文件与之前的对比，只保存不同的文件）
     - 每次生成的commit，将其根目录的tree与已有的最新commit的tree的key进行比较，发现不相同时（即文件发生了变动）添加一个commit对象，更新HEAD文件中存储的内容

 **创建一个commit类**

   - 属性 privat
      - tree的key
      - parent的key
      - author
      - committer
      - note（注释）
      - timestamp（时间戳）
      - commit的key
      - 要生成的commit文件的文件内容stringBuilder
  
   - 构造
      - 创建默认commit的无参构造方法
      - 创建指定tree 和 parent 的构造方法

   - 方法
      - get/set方法
      - 创建commit对应的文件
      - tostring方法（返回描述commit对象的字符串）



### 单元测试 Junit



**第一个检测对应的哈希值**

使用Junit的断言方法来进行测试

- 在@Before中实例化DFSFolder对象，创建文件夹以及一些子文件和子文件夹等
- 在@Test中使用断言的方式验证查找的功能，使用`assertEquals(expected, actual)` （还没研究完不知道可行性如何）查看
- 在@After中释放资源



**第二个Commit功能的单元测试**

- 编写一些修改文件的方法

- 在@Test中提交调用修改文件的方法进行几次不同的commit，通过断言判断结果是否正确