# graph_utils
## Interpretation: construction of connected-graph-format data and methods for path searching, clustering methods for discrete data structure
## A. discrete 离散
### Provide the container for data and clustring methods for such data (just k-means for now)提供数据的容器，并能对数据做聚类（只实现了k-means算法）
### 1. Node--container 节点--容器
    variables：
        uniqueTag: since if we need clustering a set of data which defined by a lot of members variables, we don't expect all these members variables to go         throw the clustering methods, so we can extract and store the unique tag for these data in the container "Node" 当我们想对一组有很多成员变量描述数据做聚类时，我们不希望将整个数据结构带到聚类的方法里去循环，那么我们可以从数据中提取唯一的标记然后储存到“Node”这个容器里
        x and y: params which is needed for clustering data with coordinate 在对有坐标的数据进行聚类时，需要x和y的值
        weight: params which is needed for clustering data with values, for verbal clustering, we might put hashcode of a vocabulary here 在对有权值的数据聚类时需要传，
                比如做词汇聚类时，我们可以把编码传进来
### 2. Cluster 簇
    variables:
        geoMethod: method for calculate center 计算中心点的方法作为成员变量传入
        nodes: set of data 数据集
    methods: 
         randomPoints: randomly pick data from nodes as initial cluter, 从数据集中随机选取初始簇
         clustering: k-means looping
## B. connected graph 连接图
### To organized data in a graph structure in java and then can do breadth-first search or depth-first search in such graph. 将数据组织成图结构，并能做深搜广搜等搜索
### 1. To build a graph within GraphStructure 图结构建立 
    example:    
                Integer[] nodes = {0, 1, 2, 3};
            array of all nodes
            所有参与这个图的节点

                GraphStructure<Integer> graphStructure = new GraphStructure<>(nodes);
            build a graph
            构造图

                Integer[] nodes = {0, 1, 2, 3};
                Integer[] nodes0 = {1, 2, 3};
                Integer[] nodes1 = {0, 2, 3};
                Integer[] nodes2 = {0, 1};
                Integer[] nodes3 = {0, 1};
            these are adjacency matrix for each node in this graph or says the connected relation for each node
            这些是图中每个点的连接关系
            
                graphStructure.make(0, nodes0);
                graphStructure.make(1, nodes1);
                graphStructure.make(2, nodes2);
                graphStructure.make(3, nodes3);
            make the edge for this graph
            建立图形的边

### 2. To use the graph 使用
    example:
                graphStructure.tailed()
            get all Edge for this graph, and remove the nodes within the out_degree == 1
            获取所有边对象，并剪去出度为1的分支
 
            constructor of GraphSearch: 
                GraphSearch(GraphStructure<T> G, T root, Manipulate manipulate,T[] nodesAbandon,T target,int maximumOutDegree)
            param 参数：
                graphStrucure ---> graph needs to manipulate, 需要操作的数据们（图）
                root ---> start, 开始的节点
                manipulate ---> bfs or dfs（others such as djkstra might be done later）, 选择操作 目前是深搜或广搜，之后会扩展 比如dj算法
                nodesAbandon ----> in some situation, we might have nodes that can't be use, so we can parse this param, 在一些业务下可以传入, 在图中但不能参与搜索的点集
                target ---> end for dfs to find all paths between two nodes, 终点，在用深搜去找两节点之间的所有路径时传入 (dfs)
                maximumOutDegree ---> just as it said： maximum out_degree for a path, 路径的最大出度   (dfs)

                GraphSearch dfs = new GraphSearch(graphStructure, 2, GraphSearch.Manipulate.DEPTH_FIRST, null, 3,3);
                dfs.getAllPaths();
            using dfs to get all paths between two path, for this particular example is the paths between `2` and `3` in the graph.
            深搜遍历两节点之间所有路径，这个例子里是`2` 和 `3` 之间的所有路径
            output: [[2, 0, 3], [2, 1, 3]]

                GraphSearch bfs=new GraphSearch(graphStructure,2,GraphSearch.Manipulate.BREADTH_FIRST,null,null,0);
                List<Object> path = bfs.pathTo(3);
            using bfs to get the path between two nodes with the smallest out_degree
            两节点之间出度最小的路径
            output: [2, 0, 3]
    
