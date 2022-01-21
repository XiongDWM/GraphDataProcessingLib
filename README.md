# GraphStructure
## To organized data in a graph structure in java and then can do breadth-first search or depth-first search in such graph. 将数据组织成图形结构，并能做深搜广搜等搜索
## 1. How to build a graph within GraphStructure。杂个建
    example:    
                Object[] nodes = {0, 1, 2, 3};
            array of all nodes
            所有参与这个图的节点

                GraphStructure graphStructure = new GraphStructure(nodes);
            build a graph
            构造图

                Object[] nodes = {0, 1, 2, 3};
                Object[] nodes0 = {1, 2, 3};
                Object[] nodes1 = {0, 2, 3};
                Object[] nodes2 = {0, 1};
                Object[] nodes3 = {0, 1};
            these are adjacency matrix for each node in this graph or says the connected relation for each node
            这些是图中每个点的连接关系
            
                graphStructure.make(0, nodes0);
                graphStructure.make(1, nodes1);
                graphStructure.make(2, nodes2);
                graphStructure.make(3, nodes3);
            make the edge for this graph
            建立图形的边

## 2. To use the graph 怎么用
    example:
                graphStructure.tailed()
            get all Edge for this graph, and remove the nodes within the out_degree == 1
            获取所有边对象，并剪去出度为1的分支
 
            constructor of GraphSearch: 
                GraphSearch(GraphStructure G, Object root, Manipulate manipulate,Object[] nodesAbandon,Object target,int maximumOutDegree)
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
    