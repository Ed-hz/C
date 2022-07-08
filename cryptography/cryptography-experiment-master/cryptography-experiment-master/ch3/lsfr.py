"""
    作者：庄杰伟
    mail：hqu@alertxss.cn
"""

# 一个命令行参数的模块，用于参数输入
import argparse

def lfsr(s): # s输入为列表
    """度为8的线性移位反馈寄存器"""
    res = []
    for i in range(16):
        # 求新产生的s
        news = (s[0]+s[1]+s[3]+s[4])%2
        # 将触发器中的s0弹出，存到res中
        res.append(s.pop(0))
        # 存入
        s.append(news)

    return res

def string2XORbin(string):
    """将输入的字符串的每个字母进行异或，以二进制的列表返回"""
    res = 0
    # 将字符转为ascii进行XOR
    for s in string:
        res ^=ord(s)

    # 将异或结果转为二进制列表
    bin_res = list(bin(res))

    # 删除0b
    bin_res.pop(0)
    bin_res.pop(0)

    # 将列表逆序
    bin_res.reverse()

    # 列表长度不足8则补全
    while len(bin_res) != 8:
        bin_res.append(0)

    # 将列表中的字符转为int
    bin_res = list(map(int, bin_res))
    return bin_res

if __name__ == "__main__":
    """命令行参数"""
    parser = argparse.ArgumentParser(description='密码学第三次实验 作者：庄杰伟')
    parser.add_argument('--string', '-s', help='string属性，若没有输入，则是实验三的第一题；反之是第二题。')
    args = parser.parse_args()

    if args.string:
        s = string2XORbin(args.string)
        print("输入的字符串为：")
        print(args.string)
    else:
        s=[1,1,1,1,1,1,1,1]

    print("二进制形式为")
    for i in range(len(s)-1,-1,-1):
        print(s[i],end='')
    print()
    print("经过lsfr后的结果：")

    res = lfsr(s)
    bin_res="0b"
    for i in range(16):
        bin_res+=str(res.pop())
    hex_res = hex(int(bin_res,2)).upper()
    print(hex_res)

