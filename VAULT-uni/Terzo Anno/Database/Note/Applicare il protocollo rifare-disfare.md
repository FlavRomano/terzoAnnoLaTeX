![[Pasted image 20231105161915.png]]

Possiamo subito dire che $T1$ fa il commit prima del checkpoint, quindi va ignorata.

1. Ripercorriamo a ritroso il log per trovare l'ultimo checkpoint. Quello che troviamo è $$\texttt{CK(T2, T3, T4)}$$ dove $\{\texttt{T2, T3, T4}\}$ sono le transazioni attive al momento del checkpoint.
2. Inizializziamo gli insiemi **UNDO** e **REDO** $$\begin{aligned}\texttt{UNDO} &= \{\texttt{T2, T3, T4}\}\\\texttt{REDO} &= \{\}\end{aligned}$$  e aggiustiamoli in base al log ![[Pasted image 20231105164236.png]]
	
	- **C(T4):** Vediamo subito che la transazione $T4$  **va rifatta**, perché esegue un commit **prima del guasto**, ==**togliamola** da **UNDO** e mettiamola in **REDO**==. $$\begin{aligned}\texttt{UNDO} &= \texttt{UNDO} \setminus \{\texttt{T4}\} = \{\texttt{T2, T3}\}\\ \texttt{REDO} &= \{\texttt{T4}\}\end{aligned}$$
	- **B(T5):** La transazione $T5$ inizia dopo il checkpoint, per il momento mettiamola in **UNDO** $$\texttt{UNDO} = \{\texttt{T2, T3, T5}\}$$
	- **B(T6):** La transazione $T6$ inizia dopo il checkpoint, per il momento mettiamola in **UNDO** $$\texttt{UNDO} = \{\texttt{T2, T3, T5, T6}\}$$
	- **C(T5):** Viene fatto il **commit** della transazione $T5$, quindi ==è stata **conclusa prima del guasto**==. Togliamo $T5$ da **UNDO** e mettiamolo in **REDO** $$\begin{aligned}\texttt{UNDO} &= \texttt{UNDO} \setminus \{\texttt{T5}\} = \{\texttt{T2, T3, T6}\}\\ \texttt{REDO} &= \{\texttt{T4, T5}\}\end{aligned}$$
3. Ripercorriamo il log *all'indietro* **disfacendo** ogni azione tra le transazioni in **UNDO** fino ==all'azione **più vecchia**==: $$\begin{aligned} \color{red}\texttt{I(T2, O6, A8)} &\implies \color{red}\texttt{D(T2,\;\;O6)}\\ \color{purple}\texttt{U(T6, O6, B7, A7)} &\implies \color{purple}\texttt{U(T6, O6 = B7)}\\ \color{#8DB600}\texttt{D(T3, O5, B7)} &\implies \color{#8DB600}\texttt{I(T3, O5 = B7)}\\ \color{#8DB600}\texttt{U(T3, O3, B5, A5)} &\implies \color{#8DB600}\texttt{U(T3, O3 = B5)}\\ \color{#8DB600}\texttt{U(T3, O2, B3, A3)} &\implies \color{#8DB600}\texttt{U(T3, O2 = B3)}\\ \color{red}\texttt{U(T2, O1, B1, A1)} &\implies \color{red}\texttt{U(T3, O1 = B1)}\end{aligned}$$
4. Ripercorriamo il log *in avanti* **rifacendo** ogni azione tra le transazioni in **REDO**:  $$\begin{aligned}\color{#FCC200}\texttt{U(T4, O3, B4, A4)} &\implies \color{#FCC200} \texttt{U(T4, O3 = A4)}\\\texttt{U(T5, O4, B6, A6)} &\implies \texttt{U(T5, O4 = A6)}\end{aligned}$$

