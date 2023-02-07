function fibonacci(n: number, dic: Map<number, number>): number {
    if (n == 0)
        return 0;
    if (n == 1)
        return 1;

    const item: number | undefined = dic.get(n)
    if (item != undefined)
        return item;
    
    let fib_n: number = fibonacci(n-1, dic) + fibonacci(n-2, dic);
    dic.set(n, fib_n);
    return fib_n;
}

console.log(fibonacci(5, new Map<number, number>()))
console.log(fibonacci(7, new Map<number, number>()))