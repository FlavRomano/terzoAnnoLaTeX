class Graph:
    graph = dict()

    def appendTo(self, node: int, *childs):
        if self.graph.get(node) is None:
            self.graph[node] = [x for x in childs]
            return

    def print_path(self, parent_map, node, root):
        path = list()
        curr = node
        while curr != root:
            path.append(curr)
            curr = parent_map[curr]
        path.append(root)
        path.reverse()
        print(f"Percorso: {path}")

    def bfs(self, root: int, node: int):
        print("BFS.")
        iterazione: int = 0
        parent_map = dict()
        if root == node:
            print("Trovato")
            return root
        frontiera: list[int] = [root]
        esplorati: set[int] = set()
        while len(frontiera) > 0:
            u: int = frontiera.pop(0)
            print(f"it. {iterazione}\n\tNodo: {u}")
            esplorati.add(u)
            for figlio in self.graph.get(u):
                parent_map[figlio] = u
                if figlio not in esplorati and figlio not in frontiera:
                    if figlio == node:
                        print("Trovato")
                        self.print_path(parent_map, node, root)
                        return figlio
                frontiera.append(figlio)
            print(f"\tFrontiera: {frontiera}\n\tEsplorati: {esplorati}")
            iterazione += 1
        print("Fallimento")
        return None

    def dfs(self, root: int, node: int):
        print("DFS.")
        iterazione: int = 0
        parent_map = dict()
        if root == node:
            print("Trovato")
            return root
        frontiera: list[int] = [root]
        esplorati: set[int] = set()
        while len(frontiera) > 0:
            u: int = frontiera.pop(-1)
            print(f"it. {iterazione}\n\tNodo: {u}")
            esplorati.add(u)
            if self.graph.get(u) is not None:
                for figlio in self.graph.get(u):
                    parent_map[figlio] = u
                    if figlio not in esplorati and figlio not in frontiera:
                        if figlio == node:
                            print("Trovato")
                            self.print_path(parent_map, node, root)
                            return figlio
                    frontiera.append(figlio)
                print(f"\n\tFrontiera: {frontiera}\n\tEsplorati: {esplorati}")
            iterazione += 1
        print("Fallimento")
        return None

    def dls(self, root: int, node: int, limit: int):
        print(f"DLS. l = {limit}")
        iterazione: int = 0
        parent_map = dict()
        if root == node:
            print("Trovato")
            return root
        frontiera: list[int] = [root]
        esplorati: set[int] = set()

        while limit >= 0 and len(frontiera) > 0:
            u: int = frontiera.pop(-1)
            print(f"it. {iterazione}\n\tNodo: {u}")
            esplorati.add(u)
            if self.graph.get(u) is not None:
                for figlio in self.graph.get(u):
                    parent_map[figlio] = u
                    if figlio not in esplorati and figlio not in frontiera:
                        if figlio == node:
                            print("Trovato")
                            self.print_path(parent_map, node, root)
                            return figlio
                    frontiera.append(figlio)
                print(f"\n\tFrontiera: {frontiera}\n\tEsplorati: {esplorati}")
            iterazione += 1
            limit -= 1
        print("Non trovato")
        return len(frontiera) == 0

    def ids(self, root: int, node: int):
        limit = 1
        failed = self.dls(root, node, limit)
        while not failed:
            failed = self.dls(root, node, limit)
            limit += 1

    def __str__(self) -> str:
        s = ""
        for k in self.graph:
            tmp = f"{k} : {self.graph.get(k)}"
            s += (tmp + "\n")
        return s.strip()


g1 = Graph()
g1.appendTo("2,1", "1,1", "3,1")
g1.appendTo("1,1", "1,2", "2,1")
g1.appendTo("1,2", "1,3", "1,1")
g1.appendTo("3,1", "3,2", "2,1")
g1.appendTo("3,2", "2,2", "3,1", "3,3")
g1.appendTo("2,2", "2,3", "3,2")
g1.appendTo("2,3", "1,3", "2,2")
g1.appendTo("3,3", "3,2")

print(g1)
g1.bfs("2,1", "1,3")
