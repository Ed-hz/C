#ifndef FUNCTION_H
#define FUNCTION_H
#include <bitset>
using namespace std;
typedef bitset<8> Byte;
typedef bitset<32> Word;

/* 计算过程中需要用到的函数 */
/* 公用部分 */
void spnMulti(int* a, int p_box[16][16], int* ans);
void toBitset(string str, bitset<4>* bs);
void toInt(bitset<4>* bs, int* arr);
int converse(char ch);  //输入的消息转换成能够进行运算的数字
/* HillCipher */
void multi(int** a, int** b, int** ans, int r, int c);  //矩阵乘法
int gcd(int a, int b);                                  //计算最大公因数
void exgcd(int a, int b, int& x, int& y, int& z);  //拓展欧几里得算法
int Getdet(int** matrix, int m);                   //计算方阵的行列式
void GetAdjoint(int** matrix, int** adjmatrix, int m);  //计算伴随矩阵
bool inverse(int** m, int** inverseMatrix, int c);      //计算逆矩阵
int DevInv(int det, int m);                             //求逆元
//AES加密
int aesConverse(const char ch);
void trans(Byte** state);
void toByte(string str, Byte** bt);
Byte GFMul(Byte a, Byte b);
int SubWord(Word w);
int RotWord(Word w);
void AddRoundKey(Byte** state, Word* RoundKey, int r);
void SubBytes(Byte** state);
void ShiftRows(Byte** state);
void MixColumns(Byte** state);
void Round(Byte** state, Word* RoundKey, int r);
void output(Byte** a, int size);
void ans(Byte** cipher, int size);
//AES解密
void InvSubBytes(Byte** state);
void InvShiftRows(Byte** state);
void InvMixColumns(Byte** state);
void InvRound(Byte** state, Word* RoundKey, int r);


#endif // FUNCTION_H
