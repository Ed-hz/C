gcd_26_num = [1,3,5,7,9,11,15,17,19,21,23,25]
inverse_gcd_26_num = [1,9,21,15,3,19,7,23,11,5,17,25]

def get_key(c, encode_c):
    """爆破得到所有可能密钥对a,b"""
    gcd_26_num_b = []
    for a in gcd_26_num:
        for b in range(26):
            if c.isupper() and chr(((ord(c)-ord('A'))*a+b)%26+ord('A')) == encode_c:
                gcd_26_num_b.append(b)
            elif c.islower() and chr(((ord(c)-ord('a'))*a+b)%26+ord('a')) == encode_c:
                gcd_26_num_b.append(b)
    return gcd_26_num_b

def decode(encode_text, a, b):
    """解密"""
    decode_text = ""
    a_ = inverse_gcd_26_num[gcd_26_num.index(a)]
    for c in encode_text:
        if c.isupper():
            decode_text += chr((((ord(c)-ord('A'))-b)*a_)%26+ord('A'))
        elif c.islower():
            decode_text += chr((((ord(c)-ord('a'))-b)*a_)%26+ord('a'))
        else:
            decode_text += c
    return decode_text

if __name__ == "__main__":
    encode_text = input("请输入密文：")
    c, encode_c = input("请输入已知明密文对(用空格隔开)：").split()

    gcd_26_num_b = get_key(c, encode_c)
    for i in range(len(gcd_26_num)):
        a = gcd_26_num[i]
        b = gcd_26_num_b[i]
        decode_text = decode(encode_text, a, b)
        if " A " in decode_text:
            print("key为："+str(a)+" "+str(b))
            print("明文为：")
            print(decode_text)
            break
