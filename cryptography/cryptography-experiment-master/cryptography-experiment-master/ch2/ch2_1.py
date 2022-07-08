def encode(text, key_a, key_b):
    """加密"""
    encode_text=""

    for c in text:
        if c.islower():
            encode_text += chr(((ord(c)-ord('a'))*key_a+key_b)%26+ord('a'))
        elif c.isupper():
            encode_text += chr(((ord(c)-ord('A'))*key_a+key_b)%26+ord('A'))
        else:
            encode_text += c
    return encode_text

def gcd(a,b):
    """判断是否互质"""
    if a > b:
        return gcd(b,a)
    while b % a != 0:
        temp = a
        a = b % a
        b = temp
    return a
        
    

if __name__ == "__main__":
    text = input("请输入要加密的明文：")
    key_a, key_b = map(int, input("请输入密钥a,b(用空格割开)：").split())
    while gcd(key_a, 26) != 1:
        key_a = int(input("输入的密钥a与26不互素，请重新输入a:"))
    print("密文是：")
    print(encode(text, key_a, key_b))

