text = "1 E 12.25 2 T 9.41 3 A 8.19 4 O 7.26 5 I 7.10 6 N 7.06 7 R 6.85 8 S 6.36 9 H 4.57 10 D 3.91 11 C 3.8312 L 3.7713 M 3.34 14 P 2.89 15 U 2.58 16 F 2.2617 G 1.71 18 W 1.59 19 Y 1.58 20 B 1.4721 K 0.4122 J 0.14 23 V 1.09 24 X 0.2125 Q 0.0926 Z 0.08 "

for i in range(len(text)):
    if text[i].isalpha():
        print(text[i].lower(),end="")
