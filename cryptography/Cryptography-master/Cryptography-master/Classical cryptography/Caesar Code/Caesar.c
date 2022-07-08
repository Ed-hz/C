//凯撒密码---基于字母表的凯撒
#include<stdio.h>
#include<stdlib.h>
#include<string.h>

void Add_Answer(char *, char [], int *);
void show(char []);

void Add_Answer(char *ptr, char Alphabet[], int *o)
{
    for(int i = 0; i < strlen(ptr);i++)
    {
        for(int j = 0; j < strlen(Alphabet); j++)
        {
            if(ptr[i] == Alphabet[j] )
            {
                j = j - *o;
                if(j > 0 || j == 0)
                {
                    printf("%c", Alphabet[j]);
                    break;
                }
                else
                {
                    j = strlen(Alphabet) + j;
                    printf("%c", Alphabet[j]);
                    break;
                }
            }
            if(ptr[i] == '{' || ptr[i] == '_' || ptr[i] == '}')
            {
                printf("%c", ptr[i]);
                break;
            }
            else
            {
                continue;
            }
        }
    }
}

void show(char Alphabet[])
{
    char *ptr;
    int *o;
    o = (int *)malloc(sizeof(int));
    ptr = (char *)malloc(sizeof(char));
    printf("-----凯撒解密------\n");
    printf("请输入要解密的内容:\n");
    scanf("%s", ptr);
    printf("请输入要进行解密的密钥:\n");
    scanf("%d", o);
    printf("解密内容如下:\n");
    Add_Answer(ptr, Alphabet, o);
}

int main()
{   char Alphabet[] = "abcdefghijklmnopqrstuvwxyz";
    show(Alphabet);
    return 0;
}