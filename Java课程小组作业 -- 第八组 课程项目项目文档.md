# Java课程小组作业 -- 第八组 课程项目项目文档

项目简介



## 第一次作业 deadline -- 12/09

- 01 实现文件及文件夹的 Key-Value 生成，同时支持
  - 给定value，向存储中添加对应的key-value
  - 给定key，查找得到对应的value值
- 02 将文件夹转化为key, value 进行持久化存储

------

**实现思路：**
- key-value存储（blob tree）
  - blob：
    - key：文件内容的哈希值
    - value：文件内容（不包含文件名）
  - tree：
    - key：value的哈希值
    - value：每个子文件blob的key；每个子文件夹tree的key；子文件夹名称和子文件名称
  - 存储方式
    - key作为文件名 文件内容为value
- 将文件夹转化为key-value存储
  - 给定一个文件夹目录，将其转化成若干tree和blob
  - 深度优先遍历此目录
    - 遇到文件就转化为blob并保存
    - 遇到子文件夹就递归调用文件夹内部的子文件/文件夹最后构造tree并保存

- 运用面向对象的方法，根据文件/文件夹创建类来进行抽象出概括的类
-  创建一个接口用于类调用不同的计算哈希值的方法
- 首先创建一个抽象类用来作为父类，抽象类中定义抽象方法以供后面的子类进行实现


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

- 方法 
  - 复制文件生成新文件
  - 计算哈希值
  - 向文件中写入内容
- 创建一个类存放项目的要求实现方法
  - 方法  
    - 查询
---

## 第二次作业 deadline -- 12/16
   -  实现commit功能

**实现思路**
   - 01 commit对象（key-value的形式存储）
     - key：commit对象的哈希值
     - value： 1.提交项目的根目录**tree的key** 2.前驱**commit对象的key**（从head文件中取得） 3.代码author  4.代码committer  5.commit备注  6.commit时间戳
     - 创建一份文件，文件的内容是以上的value，根据这些value生成commit的key，作为文件的文件名
   - 02 HEAD指针
     - 建立一个文件 里面存储最新的commit的key及当前分支的信息
   - 03 commit过程
     - （新生成的commit，需要将提交的文件与之前的对比，只保存不同的文件）
     - 每次生成的commit，将其根目录的tree与已有的最新commit的tree的key进行比较，发现不相同时（即文件发生了变动）添加一个commit对象，更新HEAD文件中存储的内容

 **创建一个commit类**

   - 属性 private
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


### **单元测试 Junit**

**第一个检测对应的哈希值**

使用Junit的断言方法来进行测试

- 在@Before中实例化DFSFolder对象，创建文件夹以及一些子文件和子文件夹等
- 在@Test中使用断言的方式验证查找的功能，使用`assertEquals(expected, actual)` （还没研究完不知道可行性如何）查看
- 在@After中释放资源



**第二个Commit功能的单元测试**

- 编写一些修改文件的方法

- 在@Test中提交调用修改文件的方法进行几次不同的commit，通过断言判断结果是否正确

---

## 第三次作业 deadline -- 12/30
  - 分支
  - 回滚
  - 命令行交互
   
 **实现思路**
  + 01 分支管理
    - 分支信息的保存
      - 有哪些分支
      - 每个分支的最新commit id
      - 当前处于的分支
    - 实现过程：
      - 创建一个文件夹，文件夹命名为branch，文件夹中的每个文件是各个分支的信息（初始状态下，branch文件夹中有一个名为main的空文件）
      - 每新建一个分支，在branch文件夹中增加一个以该分支名命名的文件，每次commit将commit的key写入对应的分支文件中
      - 每次commit将其对应的分支名写入head文件中
  + 02 分支切换和回滚
     - 回滚
        - 从所有的commmit记录中找到需要回滚到哪次，即对应的commit key（git log）
        - 把commit对应的根目录Tree对象恢复成一个文件夹
          - 根据commit key查询得到commit的value
          - 从commit value中解析得到根目录tree的key
          - 恢复(path)：
            - 根据tree的key查询得到value
            - 解析value中的每一条记录，即这个tree对象所代表的文件夹内的子文件与子文件夹名称以及对应的blob/tree key
            - 对于blob，在path中创建文件，命名为相应的文件名，写入blob的value
            - 对于tree，在path中创建文件夹，命名为相应的文件夹名，递归调用恢复(path+文件夹名)
          - 更新head指针
            - 将head文件中的commit的key改为回滚到的commit的key
     - 分支切换
        - 找到要切换到的分支对应的文件，读出其中最新的commit的key
        - 把commit对应的根目录tree对象恢复成文件夹（同回滚）
        - 更新head指针
          - 将head文件中的分支记录改为切换为的分支
  + 03 命令行交互
       - 要实现的命令
          - 初始化
            - **git init** 创建第一次commit，初始化main分支
              - 创建配置文件并保存
              - DFS创建文件夹
              - 创建commit
              - 创建head文件
              - 初始化main分支
            -  **git config --global user.name "Your Name"**
               -  配置用户名
          - 提交
            - **git commit -m" "** 提交
              - 创建一个commit对象
                - 更新branch内容
                - 更新head内容
          - 创建并切换分支
            - **git branch** 查看已有分支
              - 遍历读取branch文件中所有文件的文件名
            - **git branch branchname** 创建新分支
              - 在branch文件夹中新建一个名为branchname的空文件
            - **git checkout branchname** 切换到branchname分支
              - 判断该分支是否存在
              - 进行该分支下文件状态的复现
              - 更新head中branch的内容
          - 回滚到历史版本
            - **git log** 查看所有的commit id
              - 创建一个文件，每次提交将生成的commit的key写入文件中，执行git log操作，依次读出该文件中记录的commit的key以及找到对应的commit文件读出其value
            - **git reset commit id -hard** 回滚到对应的commit状态
              - 找到对应的commit 复现
              - 更新head
      - 实现过程
        - Scanner接收用户指令
        - 通过main函数命令行参数String[] args接收用户指令
        
        
### 暂存区设计
  1. 工作区（working directory）
  2. 暂存区（staging area/index） git add .
      - 执行git add .命令
        - 将要提交的所有类型是文件的生成blob对象，放在objects中
        - 更新index.txt文件中的内容（首次commit，生成index.txt）
        - Index.txt中的每条索引记录的是blob对象的哈希值和其文件名
  3. 仓库区（repository 本地仓库） git commit
      - 执行git commit命令
         - 生成index.txt中的所有索引的tree对象 Tree对象放入objects中
         - 根据最顶层的tree生成commit对象，并记录在head和对应的branch中

  - 一些操作命令示例：
    - git rm ，把工作区中的这个文件以及暂存区中的这个文件索引都删除
    - git rm –cached ，只把暂存区中的索引删去
    - git mv file2.txt file3.txt，工作区中的文件被重命名（file2被重命名为file3），而索引区中的原file2的索引被删除掉了，然后重新添加了一条索引


