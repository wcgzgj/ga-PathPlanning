# 遗传算法在应急路径规划上的应用

## 一、应急路径规划介绍

### 1、应急路径规划和普通路径规划的区别

应急路径规划和传统的路径规划不同

传统的路径规划，例如使用 **迪杰斯特拉** 算法求最短路径的问题，其输入是固定的，且需要考虑的条件不会很复杂，但是一旦条件复杂或者数据量大起来，那么运算时间就会成倍数增长

![迪杰斯特拉算法](https://gitee.com/faro/images/raw/master/img/20220304171745.png)

而应急路径规划，因为灾情的不确定性，其路径信息是会实时更新的，并且还要考虑车辆载重、时间窗等问题，最大的区别是，应急路径规划一般的需求是车辆要能行驶到所有受灾点，还要返回起始点，这就和货郎问题十分类似了

![受灾地区的道路情况是实时更新的](https://gitee.com/faro/images/raw/master/img/20220304172043.png)



### 2、应急路径规划需要考虑的条件

应急车辆路径问题不同于传统的车辆路径优化问题，本文将构建带时间窗的多车辆路径优化问题，应急物资在救援中心，有N个应急需求点，救援中心有k辆车，优化目的是对救援中心和紧急需求点，组织适当的行车路线，使车辆有序地完成应急救援任务，在满足一定的约束条件（货物需求量、交发货时间、车辆容量限制）下，达到一定的目标（时间违背成本最小，应急效率最高)。



#### 1）受灾点紧急程度

受灾点紧急程度我们可以用级别 5-1 表示

5代表最紧急，1代表最不紧急

当计算适应度时，我们对每个位置递减的进行权值相乘，然后累加适应度

这里我要先当个预言家，其实写到后面我发现，我的模型存在个体适应度差距不大的问题，轮盘赌的作用聊胜于无

所以我故意将受灾点紧急程度的差距范围扩大为几百

后面如果数据跑出来不好看，还要看情况，调整为 k 或者 w 数量级的差距

![紧急程度数值给予](https://gitee.com/faro/images/raw/master/img/20220311120235.png)

**我们可以看下面两个例子：**

`start -> A -> B -> C -> start`

A 紧急程度：1

B 紧急程度：2

C 紧急程度：3

紧急程度适应度得分： `3*1 + 2*2 +1*3 = 10`



`start -> C -> B -> A -> start`

A 紧急程度：1

B 紧急程度：2

C 紧急程度：3

紧急程度适应度得分： `3*3 + 2*2 +1*1 = 14`

可以看到，运用这种算法，我们的适应度得分，会随着紧急受灾点的位置考前而增加



#### 2）时间窗

假设 A 点对物资的需求时间在  0-9 内

那么如果物资送达时间为 0-9 之间，则适应度分数不减

如果超过 9 ，我们就会依照超出的时间，减去超时对应权值，对适应度得分做减法

**例：**

```java
TIME_WINDOW_WEIGHT = 10;// 时间适应度权值

carrArriveTime = 11; // 车辆到达时间
srart = 0;// 时间窗起始
end = 9;// 时间窗终止
score = score - (carrArriveTime-end)*TIME_WINDOW_WEIGHT;// 对适应度分数做减法
```

在本程序中，为了计算方便，我们的时间窗起始时间始终设置为 0



#### 3）受灾点物资重量需求是否满足

车辆初始载重我们都设置为 10 t

不同受灾点对物资的需求量是不同的

这里有两个策略：**强硬策略**和**缓和策略**

* **缓和策略：**

如果在后期出现剩余载重不满足受灾点的情况，我们需要依照情况，对适应度得分做减法

这样好处在于不容易陷入局部最优，坏处在于可能给出的结果不满足现实需求，决策者无法更具给出的数据动态为每辆车分配不用的载重

* **强硬策略：**

如果在后期出现剩余载重不满足受灾点的情况，我们直接将这种情况排除

好处在于决策者能根据信息，做出更优的判断，坏处在于给出的数据可能无法满足强硬策略的条件，导致出不了结果

<hr/>

这里我们选择缓和策略，以避免出现程序阻塞为主



#### 4）注意点

**1、**这里我们需要注意，最后算出来的适应度得分，必须是正数

下一步交叉时，就无法使用轮盘赌法选择合适的父代了

为了保证是正数，我们可以对计算结果进行补偿（加上一个统一的数），以保证轮盘赌算法能正常执行



**2、**适应度函数要保证个体适应度差距比较大，不然轮盘赌就和随机算法没什么区别了





## 二、遗传算法介绍

遗传算法，我之前写过一篇文章进行介绍

https://mp.weixin.qq.com/s/_mUK2mdczU4x_lekpuh6hw

在阅读下面的内容之前，如果还有对遗传算法不了解的同学，强烈建议去看一下哦



## 三、遗传算法在应急路径规划上的应用

遗传算法的执行流程，除了适应度计算之外，其他与特定问题的条件几乎是隔离开来的

我们要做的，就是想办法将应急路径规划考虑的条件，编码成遗传算法所能识别的数据类型

### 1、个体编码与种群初始化

#### 1）编码规则

针对路径规划问题，我们个体编码策略选择如下

路径名称用字母代替，字母在字母表中的位置即其单片染色体的编码，**数字0 表示起始点**

**例如：**

`start -> A -> B -> C -> start`

**对应的编码为：**

`0 1 2 3 0`



#### 2）编码注意事项

我们的编码还要符合路径规划的要求，即：

**1、**基因中起始点0个数 为 carNum +1

**2、**每个点只能出现一次，且所有点都必须出现

**3、**两个起始点不可以相邻

**4、**基因收尾必须是起始点位置 0

```
假设 carNum = 2  pointNum =4
正确的基因:
0 2 3 0 1 4 0
错误的基因：
0 3 3 0 1 4 0  (3受灾点重复)
0 0 3 2 1 4 0  (有两个起始点重合->有车子没出发)
```



#### 3）种群初始化

```
GeneticAlgorithm/init()
	-	Chromosome() // constructor
		-	Chromosome/init()
```



* **种群初始化：**

种群初始化直接调用个体的构造函数

个体带参构造函数里有初始化方法

```java
private void init() {
  for (int i = 0; i < POP_SIZE ;i++) {
    Chromosome chromosome = new Chromosome(CAR_NUM, POINT_NUM);
    pop.add(chromosome);
  }
}
```



* **个体初始化：**

`init()` 方法确保生成的个体合法

```java
public Chromosome(int carNum, int pointNum) {
  this.carNum=carNum;
  this.pointNum=pointNum;
  geneSize = carNum+1+pointNum;
  gene = new int[geneSize];
  for (int i = 0; i < geneSize; i++) {
    gene[i]=-1;
  }
  init();
}

// ...

public void init() {
  int remainStartPoints = carNum + 1;
  gene[0]=0;
  gene[geneSize-1]=0;
  remainStartPoints-=2;
  // 存放未被放置的受灾点
  List<Integer> pointList = new ArrayList<>();
  for (int i = 1; i <= pointNum ; i++) {
    pointList.add(i);
  }
  // 随机生成起始点
  if (carNum>1 && geneSize>4) {
    while (remainStartPoints>0) {
      // 随机基因起始点
      int tmpStartIndex = r.nextInt(geneSize - 4) + 2;
      if (gene[tmpStartIndex]!=0 && gene[tmpStartIndex-1]!=0 && gene[tmpStartIndex+1]!=0) {
        gene[tmpStartIndex]=0;
        remainStartPoints--;
      }
    }
  }

  for (int i = 0; i < geneSize; i++) {
    if (gene[i]==-1) {
      int randomIndex = 0;
      if (pointList.size()>0) {
        randomIndex = r.nextInt(pointList.size());
      }
      gene[i]=pointList.get(randomIndex);
      pointList.remove(randomIndex);
    }
  }
}
```



* **判断生成的个体是否合法：**

```java
public static boolean isGoodChromosome(Chromosome chromosome) {
  // 1、基因不存在
  if (chromosome==null || chromosome.getGene()==null || chromosome.getGene().length==0) return false;
  int[] gene = chromosome.getGene();
  List<Integer> genePointList = new ArrayList<>();
  // 基因中目标起始点的个数
  int targetStartPointNum = chromosome.getCarNum() + 1;
  int startPointCount = 2;
  if (gene[gene.length-2]==0) return false;
  for (int i = 1; i < gene.length-1; i++) {
    if (gene[i]==0 && gene[i-1]==0) {
      // 2、两个起始点邻接
      return false;
    }
    if (gene[i]==0) {
      startPointCount++;
    }
    else {
      genePointList.add(gene[i]);
    }
  }
  // 3、起始点个数不满足要求
  if (startPointCount!=targetStartPointNum) return false;
  // 4、不满足囊括所有受灾点
  // 受灾点排布必须满足 n=N 时，为  1,2,...N
  genePointList.sort((o1, o2) -> o1-o2);
  if (genePointList.get(0)!=1) return false;
  for (int i = 1; i <genePointList.size() ; i++) {
    if (genePointList.get(i)-1!=genePointList.get(i-1)) return false;
  }
  return true;
}
```

<hr/>

**经测试，初始化函数是严格符合要求的：**

```java
@Test
public void testInit() {
  List<Chromosome> list = new ArrayList<>();
  for (int i = 0; i < 500; i++) {
    list.add(new Chromosome(5,30));
  }
  for (Chromosome chromosome : list) {
    System.out.println(chromosome);
    System.out.println(Chromosome.isGoodChromosome(chromosome));
  }
}
```

![测试结果](https://gitee.com/faro/images/raw/master/img/20220311131306.png)



### 2、适应度计算

```
calculatePopScore()
	- calculateScore()
		- decodeGene()
```

* **种群适应度计算：**

种群适应度计算，只需遍历调用个体的适应度计算函数即可

同时，还要统计当前种群中的相关信息

```java
private void calculatePopScore() {
  iterCount++;
  if (pop==null || pop.size()==0) return;
  totalScore=0;
  averageScore=0;
  bestScore=0;
  worstScore=Double.MAX_VALUE;
  bestGene=null;
  worstGene=null;
  for (Chromosome chromosome : pop) {
    calculateScore(chromosome);
    //System.out.println(chromosome);
    totalScore+=chromosome.getScore();
    if (chromosome.getScore()>bestScore) {
      bestScore=chromosome.getScore();
      bestGene= chromosome.getGene();
    }
    if (chromosome.getScore()<worstScore){
      worstScore= chromosome.getScore();
      worstGene= chromosome.getGene();
    }
    averageScore=totalScore/POP_SIZE;
    averageScore=averageScore>bestScore?bestScore:averageScore;
  }
  // 将待绘制的折线图数据信息存入 map 中
  bestScoreDataMap.put((double) iterCount,bestScore);
  worstScoreDataMap.put((double) iterCount,worstScore);
  totalScoreDataMap.put((double) iterCount,totalScore);
}
```



* **个体适应度计算：**

这里的适应度计算采取的是**温和策略**，即：

**1、**不在时间窗内到达的话，不会舍去该路径

**2、**车辆货物余额不满足受灾点要求的话，不会舍去该路径

<hr/>

而是**减少适应度得分**，需要考虑：

**1、**单个车体载重能否满足所有受灾点

**2、**单个车体到达时间是否在时间窗之内

**3、**受灾点的紧急程度

```java
private void calculateScore(Chromosome chromosome) {
  if (!Chromosome.isGoodChromosome(chromosome)) {
    throw new RuntimeException("错误：当前染色体出现错误，无法计算");
  }
  // 可能染色体序列 0 5 0 3 2 1 4 0   ： start E start C B A D start

  // 已转录基因组
  // 不同车辆行驶的路径已经切分开了 : 0 5 0 3 2 1 4 0 -> [[start,E,start],
  //                                                [start,C,B,A,D,start]]
  List<List<String>> decodedGeneList = decodeGene(chromosome);
  double scoreCount = 0d;
  // 每个片段，代表一辆车的行驶路径
  for (List<String> slice : decodedGeneList) {
    // 记录车辆剩余货物重量
    double carCurrCapacity = CAR_CAPACITY;
    // 记录当前车辆已经使用的时间（方便与时间窗进行对比）
    double currCarTotalTime=0;
    // 紧急程度权值起算起点
    int emergencyStart = POINT_NUM;
    for (int i = 0; i < slice.size() - 1; i++) {
      // 获取起始点和终点信息
      Point startPoint = info.get(slice.get(i));
      Point endPoint = info.get(slice.get(i+1));

      // 计算两点之间路径长度
      double routeLength = routeLength(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
      // 行驶时间
      double routeTime = routeLength / CAR_SPEED;

      currCarTotalTime+=routeTime;
      // 1、是否符合时间窗
      if (currCarTotalTime>endPoint.getEnd()) {
        scoreCount=scoreCount-(currCarTotalTime-endPoint.getEnd())*TIME_WINDOW_WEIGHT;
      }
      // 当前车辆运行时间还要加上受灾点需要的服务时间
      currCarTotalTime+=endPoint.getServiceTime();
      // 2、车辆运载的货物重量是否满足受灾点需求
      if (carCurrCapacity<endPoint.getNeed()) {
        scoreCount=scoreCount-(endPoint.getNeed()-carCurrCapacity)*GOOD_NEED_WEIGHT;
        carCurrCapacity=0;
      }
      // 减去当前受灾点消耗掉的货物
      carCurrCapacity-=endPoint.getNeed();
      // 3、计算车辆紧急程度权值
      scoreCount+=endPoint.getEmergency()*emergencyStart--*EMERGENCY_WEIGHT;
    }
  }
  scoreCount+=ORIGIN_SCORE;
  //System.out.println("score为："+scoreCount);
  // 如果适应度分数为0，要做补偿
  scoreCount = Math.max(0,scoreCount);
  chromosome.setScore(scoreCount);
}
```





### 3、父代交叉与下一代种群的生成

```
evolve()
	- getParentChromosome() // 择优获取父代
	-	genetic() // 父代交叉
		- getPossibleGeneticPair() // 获取可能的交叉位置
			- isSwapScopeLegal() // 判断交叉位置合法性
	- isGoodChromosome() // 判断生成的子代是否满足要求
```



下一代种群生成其中一个不可回避的过程，就是父代交叉

* **新种群生成函数：**

新种群生成步骤有下面两个：

**1、**择优选择父代

**2、**父代交叉生成子代

```java
private void evolve() {
  List<Chromosome> newPop = new ArrayList<>();
  while (newPop.size()<POP_SIZE) {
    Chromosome p1 = getParentChromosome();
    Chromosome p2 = getParentChromosome();
    // genetic 保证生成的子代一定合法
    List<Chromosome> children = Chromosome.genetic(p1, p2);
    for (Chromosome child : children) {
      if (Chromosome.isGoodChromosome(child)) {
        newPop.add(child);
      }
    }
  }
  // 保证新生成的子代长度与原父代长度相等
  while (newPop.size()>POP_SIZE) {
    newPop.remove(r.nextInt(newPop.size()));
  }
  pop.clear();
  pop=newPop;
}
```



* **择优选择父代：**

这里我们使用轮盘赌算法选择父代

```java
private Chromosome getParentChromosome() {
  // 轮盘赌选中的部分
  double slice = Math.random() * totalScore;
  //System.out.println("轮盘赌 slice:"+slice);
  //System.out.println("轮盘赌 total:"+totalScore);
  double sum = 0d;
  //int targetCount =0;
  for (Chromosome chromosome : pop) {
    //targetCount++;
    sum+=chromosome.getScore();
    // 轮盘赌选中
    if (sum>slice) {
      //System.out.println("当前命中的目标为："+targetCount);
      return chromosome;
    }
    //System.out.println("轮盘赌选中个体为:\n"+chromosome);
  }
  return pop.get(pop.size()-1);
}
```



* **交叉算法：**

```java
public static List<Chromosome> genetic(Chromosome p1,Chromosome p2) {
  if (p1==null || p2==null) return null;
  if (p1.getGeneSize()!=p2.getGeneSize()) return null;
  List<Chromosome> children = new ArrayList<>();

  // 起始点可能的集合（后期起始点可能不会设置为0）
  Set<Integer> startPointSet = new HashSet<>();
  startPointSet.add(0);
  List<Pair<Integer, Integer>> possibleGeneticPair = getPossibleGeneticPair(p1, p2, startPointSet);
  // 获取交叉位置
  Pair<Integer, Integer> pair = possibleGeneticPair.get(r.nextInt(possibleGeneticPair.size()));
  Chromosome c1 = p1.clone();
  Chromosome c2 = p2.clone();
  int[] c1Gene = c1.getGene();
  int[] c2Gene = c2.getGene();
  int left = pair.fst;
  int right = pair.snd;
  for (int i = left; i <=right ; i++) {
    int tmp = c1Gene[i];
    c1Gene[i]=c2Gene[i];
    c2Gene[i]=tmp;
  }
  children.add(c1);
  children.add(c2);
  return children;
}
```



* **获取可能的交叉位置：**

这里有两种方式选择可交叉位置：

**1、**先将所有可能交叉的位置选出来，在随机选择

**2、**随机生成交叉位置，再进行判断

这里我们之所以**选择方案1**，是因为这种方式命中率最高，主要原因还是在染色体限制条件下，可交换位置还是比较少的

**方案一的时间复杂度，稳定在 O(n^2)**

**但是方案二的时间复杂度，在 O(1) - O(∞) 中徘徊**

经过我的测试，**方案2**平均耗时是**方案1**的两倍以上

```java
public static List<Pair<Integer,Integer>> getPossibleGeneticPair(Chromosome p1,Chromosome p2,Set<Integer>startPointSet) {
  if (p1==null || p2==null) return null;
  if (p1.getGeneSize()!=p2.getGeneSize()) return null;
  List<Pair<Integer, Integer>> res = new ArrayList<>();
  int geneSize = p1.getGeneSize();
  // 暂时不包含首尾
  for (int l = 1; l <geneSize-1 ; l++) {
    for (int r = l; r <geneSize-1 ; r++) {
      if (isSwapScopeLegal(l,r,p1,p2,startPointSet)) {
        res.add(new Pair<>(l,r));
      }
    }
  }
  return res;
}
```



### 4、种群变异

```
GenaticAlgorithm/mutation()
	- Chromosome/mutation()
```

* **种群变异：**

变异也不是每个个体都要发生变异的

我们设置变异概率，使变异发生在一个合理的范围内

```java
private void mutation() {
  for (Chromosome chromosome : pop) {
    if (Math.random()<MUTATION_RATE) {
      chromosome.mutation(MAX_MUTATION_NUM);
    }
  }
}
```



* **个体变异：**

个体变异同样要保证变异后的染色体合法性

这里我们也有两种策略：

**1、**先将所有可能变异的位置选出来，在随机选择

**2、**随机生成变异位置，再进行判断

这里和交叉不一样，我们选择

```java
public void mutation(int maxMutationNum) {
  // 如果要变异，变异对的个数起码也得是1对
  int mutationPairNum = Math.max((int) Math.random() * maxMutationNum / 2,1);
  // 变异策略有两种
  // 1、找出所有合理变异对，然后选择
  // 2、随机选择变异对，然后判断是否合理
  // 这里选择第二种，因为其命中概率不低，不会造成太高的迭代
  int left = r.nextInt(geneSize - 2) + 1;
  int right = r.nextInt(geneSize - 2) + 1;
  // 防止出现过高迭代，导致阻塞
  int maxIterNum = 400;
  while (!isSwapPairLegal(left,right) && maxIterNum>=0) {
    left = r.nextInt(geneSize - 2) + 1;
    right = r.nextInt(geneSize - 2) + 1;
    maxIterNum--;
    if (maxIterNum<0) {
      left=0;
      right=0;
    }
  }
  // 交换目标基因
  int tmp = gene[left];
  gene[left] = gene[right];
  gene[right]=tmp;
}
```



### 5、折线图绘制

使用图标能最直观的反应我们的数据走势

需要下面这两张图

* 总适应度变化折线图
* 最佳适应度-最坏适应度变化图



#### 1）折线图工具类

这里我们使用 **jfree** 绘制折线图

* **导入 Maven 依赖**

```xml
<dependency>
  <groupId>jfree</groupId>
  <artifactId>jfreechart</artifactId>
  <version>1.0.13</version>
</dependency>
```



* **编写工具函数**

工具函数我已经封装好了，各位直接拷到自己项目里就行

```java
public class DrawingTools extends ApplicationFrame {
  private String titleFont;
  private int titleFontSize;
  private String xyFont;
  private int xyFontSize;

  DrawingTools() {
    this("Charts");
  }

  public DrawingTools(String appTitle) {
    super(appTitle);
    this.titleFont = "微软雅黑";
    this.titleFontSize = 20;
    this.xyFont = "微软雅黑";
    this.xyFontSize = 15;
  }

  /**
     * @param appTitle    标题
     * @param chartTitle  图标题
     * @param xName       x轴命名
     * @param yName       y轴命名
     * @param dataSet     数据集
     * @param types       线条种类
     */
  public static void drawLineChart(String appTitle, String chartTitle,
                                   String xName,
                                   String yName,
                                   Map<Double, Double>[] dataSet,
                                   String[] types) {
    DrawingTools tools = new DrawingTools(appTitle);
    IntervalXYDataset dataset = tools.getLineDataset(dataSet, types);
    JFreeChart chart = tools.getLineChart(chartTitle, xName, yName, dataset);

    //绘图模式化
    tools.setChartCSS(chart);
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new java.awt.Dimension(900, 600));
    tools.setContentPane(chartPanel);
    tools.pack();
    RefineryUtilities.centerFrameOnScreen(tools);
    tools.setVisible(true);
  }

  private JFreeChart getLineChart(String title, String xName, String yName, XYDataset dataset) {
    /**
         * 图标标题，x轴名称，y轴名称，数据集合，图标显示方向，是否使用图示，是否生成工具栏，是否生成URL链接
         */
    JFreeChart chart = ChartFactory.createXYLineChart(
      title,
      xName,
      yName,
      dataset,
      PlotOrientation.VERTICAL,
      true,
      true,
      false
    );
    return chart;
  }



  /**
     * 自定义设置图表字体样式
     *
     * @param chart
     */
  private void setChartCSS(JFreeChart chart) {
    //初始化
    chart.setBackgroundPaint(ChartColor.WHITE);
    XYPlot plot = chart.getXYPlot();

    //标题
    TextTitle textTitle = chart.getTitle();
    textTitle.setFont(new Font(titleFont, Font.BOLD, titleFontSize));
    LegendTitle legendTitle = chart.getLegend();
    legendTitle.setItemFont(new Font(titleFont, Font.PLAIN, titleFontSize));


    //图表xy轴字体设置
    plot.getDomainAxis().setLabelFont(new Font(xyFont, Font.PLAIN, xyFontSize));
    plot.getDomainAxis().setTickLabelFont(new Font(xyFont, Font.PLAIN, xyFontSize));
    plot.getRangeAxis().setTickLabelFont(new Font(xyFont, Font.PLAIN, xyFontSize));
    plot.getRangeAxis().setLabelFont(new Font(xyFont, Font.PLAIN, xyFontSize));

    //设置背景色-xy轴格子色
    plot.setBackgroundPaint(ChartColor.WHITE);
    plot.setRangeGridlinePaint(ChartColor.lightGray);
    //        plot.setDomainGridlinePaint(ChartColor.lightGray);

    //折线图渲染
    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    plot.setRenderer(renderer);
    //        renderer.setPaint(ChartColor.BLACK);
    chart.getLegend().setPosition(RectangleEdge.RIGHT);

  }

  /**
     * @param dataSets int:double
     * @param types    折线的种类
     * @return
     */
  private IntervalXYDataset getLineDataset(Map<Double, Double>[] dataSets, String[] types) {

    XYSeriesCollection dataSet = new XYSeriesCollection();
    int index = 0;
    for (String type : types) {
      XYSeries series = new XYSeries(type);
      for (Map.Entry<Double, Double> data : dataSets[index++].entrySet()) {
        series.add(data.getKey(), data.getValue());
      }
      dataSet.addSeries(series);
    }
    return dataSet;
  }
}
```



#### 2）封装工具类，使其符合路径规划问题

```java
/**
 * 绘制总适应度函数变化折线图
 * @param dataSet
 */
public static void drawTotalScoreGraph(Map[] dataSet) {
  String[] types = {"总适应度"};
  DrawingTools.drawLineChart("总适应度变化","总适应度变化","迭代次数","适应度得分",dataSet,types);
}


/**
 * 绘制最优/最差 适应度变化折线图
 * @param dataSet 绘制图标使用的数据集
 */
public static void drawBestWorstScoreGraph(Map[] dataSet) {
  String[] types = {"最佳适应度", "最坏适应度"};
  DrawingTools.drawLineChart("最佳/最坏适应度变化","最佳/最坏适应度变化","迭代次数","适应度得分",dataSet,types);
}
```





## 四、测试、参数调整与数据分析

### 1、测试

![测试结果1](https://gitee.com/faro/images/raw/master/img/20220311152828.jpg)

![测试结果2](https://gitee.com/faro/images/raw/master/img/20220311152835.jpg)

经过上述测试，我们发现，计算结果并没有按照理想的曲线递增，而且个体适应度分数区别不大，这样

轮盘赌就近似与随机算法了，只有**大幅提高种群个数**才能抹平这种差异

关于这点，我会在下面的参数调整部分细讲，测试部分我们需要保证该算法是切实有效的

这时候参数写在配置文件中的好处就开始凸显了

![待测试的部分](https://gitee.com/faro/images/raw/master/img/20220311154413.png)



* **仅针对时间窗权重进行计算：**

可以发现，总适应度是先呈现一个上升趋势，然后有轻微下降，最后开始区域平稳的

说明单纯考虑时间窗对于该算法是有效的

![2](https://gitee.com/faro/images/raw/master/img/20220311160446.jpg)

![1](https://gitee.com/faro/images/raw/master/img/20220311160439.jpg)



* **仅针对载重进行考虑：**

以载重为考虑先决条件的话，总适应度变化不大

可能是与我们的补偿分数设置太大有关

但是最佳/最坏适应度变化曲线符合遗传算法的要求，可以判定对载重的判断正确

![2](https://gitee.com/faro/images/raw/master/img/20220311160644.jpg)

![1](https://gitee.com/faro/images/raw/master/img/20220311160637.jpg)



* **仅针对应急程度考虑：**

**针对应急程度考虑**的话，其变化规律与**仅考虑时间窗**一致

可以判定其运转正常

![1](https://gitee.com/faro/images/raw/master/img/20220311160842.jpg)

![2](https://gitee.com/faro/images/raw/master/img/20220311160847.jpg)





### 2）参数调整

但是我们要清楚，为了让计算时间在可接受范围，**种群个数和迭代次数都必须限定在一定范围内！！**

而且，经过上面的测试，我们发现，在针对部分参数的考量中，折线变化不明显，这会严重影响决策者的判断

所以，我们要对参数进行调整

* **基于种群参数设置**

```properties
#种群大小
#种群在达到 1000 的时候，已经开始出现阻塞现象了
#POP_SIZE=1000
POP_SIZE=200

#基因长短（在路径规划中，基因长短是与路径条数和车辆个数有关的，没法单独配置）
#CHROMOSOME_SIZE=6

#迭代次数
ITER_NUM=200
```



* **基于显示情况考虑的权值设置**

```properties
# 时间窗权重
TIME_WINDOW_WEIGHT=10
#TIME_WINDOW_WEIGHT=0

# 货物需求点权重
GOOD_NEED_WEIGHT=10
#GOOD_NEED_WEIGHT=0

# 补偿得分
#ORIGIN_SCORE=0
ORIGIN_SCORE=0

# 应急点紧急程度对应的权值
EMERGENCY_WEIGHT=5
#EMERGENCY_WEIGHT=5
```



* **基于变异情况的设置：**

因为我们的代码中，先期已经对大量不合理的情况进行了排除

所以我们的种群缺少多样性，导致迭代次数超过一定阈值后，会出现大量相似个体

这个时候如果变异概率太低，会导致个体逐渐趋同，影响结果的产生

**小变异产生的结果：**

![小变异1](https://gitee.com/faro/images/raw/master/img/20220311161942.jpg)

![小变异2](https://gitee.com/faro/images/raw/master/img/20220311161947.jpg)



**大变异差生的结果：**

可以看到，大变异后产生的个体明显更具差异性，其获得相对最优值的概率也越高

![大变异](https://gitee.com/faro/images/raw/master/img/20220311162021.jpg)

![大变异2](https://gitee.com/faro/images/raw/master/img/20220311162025.jpg)

```properties
#变异概率(调大调小变异概率)
MUTATION_RATE=0.1

#最大变异长度 这个和基因长度有关
#MAX_MUTATION_NUM=3
```



### 3）数据分析

接下来，我们放开所有参数条件，看数据折现是什么情况的

理想情况下，应该是**总适应度呈上升态势**

最佳适应度应该是由一个高位，逐渐递减，最后趋于缓和

最坏适应度应该是由低位逐渐上升，最后区域缓和

但是最佳、最坏二者最后应该是收敛域同一个数据范围的

**手绘一下的话，应该是这个表现形式：**

![变化曲线](https://gitee.com/faro/images/raw/master/img/20220311165424.png)



**最后事实也确实如我所料，折线走向和我预期的一致：**

![1](https://gitee.com/faro/images/raw/master/img/20220311165503.jpg)

![2](https://gitee.com/faro/images/raw/master/img/20220311165509.jpg)

这样我大致可以判断，算法和参数大致是符合路径规划需求的

**最后获取的最佳个体基因如下：**

`[0, 20, 5, 8, 16, 23, 10, 24, 0, 3, 13, 2, 11, 4, 15, 12, 21, 14, 7, 0, 22, 6, 17, 9, 26, 18, 19, 1, 0, 25, 0]`

为了图方便，我的数据直接用 Map 存的，**其具体参数如下：**

```java
public class Info {
  private static Map<String, Point> info = new HashMap<>();
  static {
    info.put("start",new Point("start",100d,100d,0d,0d,0d,Double.MAX_VALUE,0)); // 起始点信息
    info.put("A",new Point("A",160d,180d,5.6d,5.18d,0d,3.4d,7));
    info.put("B",new Point("B",170d,50d,9.8d,1.48d,0d,7.1d,190));
    info.put("C",new Point("C",30d,120d,1.3d,3.78d,0d,5.6d,2));
    info.put("D",new Point("D",60d,90d,4.7d,1.08d,0d,3.2d,41));
    info.put("E",new Point("E",250d,30d,8.3d,3.38d,0d,10d,50));
    info.put("F",new Point("F",190d,110d,1.4d,3.68d,0d,6.2d,23));
    info.put("G",new Point("G",80d,130d,4.7d,1.98d,0d,3.8d,4));
    info.put("H",new Point("H",140d,50d,2.8d,2.28d,0d,4.7d,420));
    info.put("I",new Point("I",30d,20d,4.6d,2.58d,0d,7d,10));
    info.put("J",new Point("J",10d,70d,2.6d,2.88d,0d,6d,1));
    info.put("K",new Point("K",70d,120d,2.6d,3.44d,0d,4d,100));
    info.put("L",new Point("L",90d,170d,4.6d,5.88d,0d,9d,1));
    info.put("M",new Point("M",30d,180d,2.6d,3.24d,0d,3d,1));
    info.put("N",new Point("N",20d,150d,6.6d,1.89d,0d,3d,9));
    info.put("O",new Point("O",90d,140d,2.6d,5.88d,0d,5.3d,122));
    info.put("P",new Point("P",160d,40d,7.6d,4.88d,0d,9d,39));


    info.put("Q",new Point("Q",190d,80d,2.6d,2.97d,0d,2d,210));
    info.put("R",new Point("R",140d,150d,5.6d,3.65d,0d,4d,33));
    info.put("S",new Point("S",70d,50d,8.6d,1.97d,0d,5d,23));
    info.put("T",new Point("T",200d,50d,6.6d,1.48d,0d,1d,723));
    info.put("U",new Point("U",220d,90d,2.6d,4.88d,0d,2d,34));
    info.put("V",new Point("V",40d,60d,7.6d,1.88d,0d,2d,782));
    info.put("W",new Point("W",90d,110d,9.6d,0.88d,0d,4d,123));
    info.put("X",new Point("X",0d,80d,8.6d,2.89d,0d,2d,73));
    info.put("Y",new Point("Y",10d,120d,2.6d,1.49d,0d,6d,35));
    info.put("Z",new Point("Z",40d,150d,9.6d,4.81d,0d,9d,92));
  }

  public static Map<String, Point> getInfo() {
    return info;
  }
}
```

```java
public class Point {
  // 受灾点名称
  private String name;
  // 受灾点坐标
  private double x;
  private double y;
  // 受灾点所需物资
  private double need;
  // 受灾点服务时间
  private double serviceTime;
  // 受灾点时间窗
  private double start;
  private double end;
  // 紧急程度
  private int emergency;
}
```

