function brainLuck(code: string, input: string) {
    var output: string[] = [];
    var byteArray: number[] = new Array(255);
    var inputByte: number[] = input.split("").map(char => char.charCodeAt(0));
    var inputPointer: number = 0;
    var pointer: number = 0;
    
    let i: number = 0;
    var loopIndex: number = 0;
    while (i < code.length) {
        let val: number = byteArray[pointer] != undefined ? byteArray[pointer] : 0;
        let keyword = code[i];
        console.log(keyword, val)
        switch (keyword) {
            case ">":
                ++pointer;
                break;
            case "<":
                --pointer;
                break;
            case "+":
                byteArray[pointer] = val == 255 ? 0 : val + 1;
                break;
            case "-":
                byteArray[pointer] = val == 0 ? 255 : val - 1;
                break;
            case ".":
                output.push(String.fromCharCode(val))
                break;
            case ",":
                byteArray[pointer] = inputByte[inputPointer++];
                break;
            case "[":
                if (byteArray[pointer] != 0)
                    loopIndex = i;
                break;
            case "]":
                if (byteArray[pointer] != 0)
                    i = loopIndex;
                break;
            default:
                break;
        }
        ++i;
    }
    return output.join("");
}

console.log(brainLuck(",[.[-],]", "Codewars"))