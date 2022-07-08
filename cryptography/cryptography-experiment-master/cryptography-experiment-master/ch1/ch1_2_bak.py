def lower_to_capital(dict_info):
    new_dict = {}
    for i, j in dict_info.items():
        new_dict[i.upper()] = j.upper()
    return new_dict


def get_count(text):
    """分析字符中的字母出现次数"""
    count = {}
    num=0
    for i in range(len(text)):
        if text[i].isalpha():
            if text[i].lower() in count:
                count[text[i].lower()]+=1
            else:
                num+=1
                count[text[i].lower()]=1
    # sorted(可迭代对象,key=函数名,reverse=False/True)
    # 排序
    count_order=sorted(count.items(),key=lambda x:x[1],reverse=True)
    print(str(num))
    result = ''
    for i in count_order:
        result += i[0]
    print(result)
    return result

if __name__ == "__main__":

    text = input("text:")
    text_frequency=get_count(text)
    frequency = "etaoinrshdclmpufgwybkjvxqz"

    key={}

    for i in range(25):
        key[text_frequency[i]]=frequency[i]
    KEY = lower_to_capital(key)
    # print(key)
    result = ""
    for c in text:
        if c.isalpha():
            if c in key.keys():
                c = key[c]
            elif c in KEY.keys():
                c = KEY[c]
        result+=c
    print(result)
