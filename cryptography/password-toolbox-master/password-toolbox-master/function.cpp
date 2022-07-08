#include <function.h>
#include <crypto.h>
using namespace std;

/* 计算相关函数的实现 */

void toBitset(string str, bitset<4>* bs) {
  int j = 0;
  for (int i = 0; i < (int)str.size(); i += 4) {
    string temp = str.substr(i,4);
    bitset<4> bst(temp);
    bs[j++] = bst;
  }
}

void toInt(bitset<4>* bs, int* arr) {
  for (int i = 0; i < 4; i++) {
    string str = bs[i].to_string();
    for (int j = 0; j < 4; j++) arr[i * 4 + j] = (int)str[j] - (int)'0';
  }
}

void spnMulti(int* a, const int p_box[16][16], int* ans) {
  for (int i = 0; i < 1; i++) {
    for (int j = 0; j < 16; j++) {
      ans[j] = 0;
      for (int k = 0; k < 16; k++) ans[j] += a[k] * p_box[k][j];
    }
  }
}

int converse(char ch) {
  int trans = 0;
  if (ch >= 'a' && ch <= 'z')
    trans = (int)ch - (int)'a';
  else if (ch >= 'A' && ch <= 'Z')
    trans = (int)ch - (int)'A';
  else if (ch == ' ')
    trans = 26;
  return trans;
}
void multi(int** a, int** b, int** ans, int r, int c) {
  for (int i = 0; i < r; i++)
    for (int j = 0; j < c; j++) {
      ans[i][j] = 0;
      for (int k = 0; k < c; k++) ans[i][j] += a[i][k] * b[k][j];
      ans[i][j] = (ans[i][j] % 27 + 27) % 27;
    }
}
int gcd(int a, int b) {
  if (b == 0)
    return a;
  else
    return gcd(b, a % b);
}
void exgcd(int a, int b, int& x, int& y,
                   int& z) {  //拓展欧几里得算法
  if (!b) {
    x = a;
    y = 1;
    z = 0;
  } else {
    exgcd(b, a % b, x, z, y);
    z -= y * (a / b);
  }
}
int Getdet(int** matrix, int m) {
  if (m == 1) return matrix[0][0];
  int ans = 0;
  int** temp = new int*[m];
  for (int i = 0; i < m; i++) temp[i] = new int[m];
  for (int i = 0; i < m; i++) {
    for (int j = 0; j < m - 1; j++) {
      for (int k = 0; k < m - 1; k++)
        temp[j][k] = matrix[j + 1][(k >= i) ? k + 1 : k];
    }
    int t = Getdet(temp, m - 1);
    if (i % 2 == 0)
      ans += matrix[0][i] * t;
    else
      ans -= matrix[0][i] * t;
  }
  for (int i = 0; i < m; i++) delete[] temp[i];
  delete[] temp;
  return ans;
}
void GetAdjoint(int** matrix, int** adjmatrix, int m) {
  if (m == 1) adjmatrix[0][0] = 1;
  int** temp = new int*[m];
  for (int i = 0; i < m; i++) temp[i] = new int[m];
  for (int i = 0; i < m; i++) {
    for (int j = 0; j < m; j++) {
      for (int k = 0; k < m - 1; k++) {
        for (int n = 0; n < m - 1; n++) {
          temp[k][n] = matrix[k >= i ? k + 1 : k][n >= j ? n + 1 : n];
        }
      }
      adjmatrix[j][i] = Getdet(temp, m - 1);
      if ((i + j) % 2 == 1) adjmatrix[j][i] = -adjmatrix[j][i];
    }
  }
  for (int i = 0; i < m; i++) delete[] temp[i];
  delete[] temp;
}

bool inverse(int** Key, int** inverseKey, int c) {
  int** adjmatrix = new int*[c];
  for (int i = 0; i < c; i++) adjmatrix[i] = new int[c];
  int det = (Getdet(Key, c) % 27 + 27) % 27;
  if (det == 0 || gcd(det, 27) != 1) {
     //窗体
      return false;
  } else {
    int detInv = DevInv(det, 27);
    GetAdjoint(Key, adjmatrix, c);
    for (int i = 0; i < c; i++)
      for (int j = 0; j < c; j++) {
        inverseKey[i][j] = 0;
        inverseKey[i][j] =
            ((detInv * ((adjmatrix[i][j] % 27 + 27) % 27)) % 27 + 27) % 27;
      }
    for (int i = 0; i < c; i++) delete[] adjmatrix[i];
    delete[] adjmatrix;
    return true;
  }
}
int DevInv(int det, int m) {
  int x, y, z;
  exgcd(det, m, x, y, z);
  return x == 1 ? (y + m) % m : -1;
}

//AES
int aesConverse(const char ch){
    int trans = 0;
    if (ch >= 'a' && ch <= 'z')
      trans = (int)ch - (int)'a' + 10;
    else if (ch >= '0' && ch <= '9')
      trans = (int)ch - (int)'0';
    else if(ch>='A'&&ch<='Z')
       trans = (int)ch-(int)'A'+10;
    return trans;
}
void trans(Byte** state){
    for (int i = 0; i < 4; i++)
        for (int j = i + 1; j < 4; j++) {
          swap(state[i][j], state[j][i]);
        }
}
void toByte(string str, Byte** bt) {
  for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 4; j++) {
      int t1 = aesConverse(str[8 * i + 2 * j]);
      int t2 = aesConverse(str[8 * i + 2 * j + 1]);
      Byte bt1(t1);
      Byte bt2(t2);
      Byte bt3 = (bt1 << 4) ^ (bt2);
      bt[i][j] = bt3;
    }
  }
}
//域中的乘法
Byte GFMul(Byte a, Byte b) {
  Byte ans = 0;
  Byte hi_bit_set;
  for (int counter = 0; counter < 8; counter++) {
    if ((b & Byte(1)) != 0) {
      ans ^= a;
    }
    hi_bit_set = (Byte)(a & Byte(0x80));
    a <<= 1;
    if (hi_bit_set != 0) {
      a ^= 0x1b; /* x^8 + x^4 + x^3 + x + 1 */
    }
    b >>= 1;
  }
  return ans;
}

//密钥编排
int SubWord(Word w) {
  Byte bt[4];
  Word ans;
  Word tmp;
  for (int i = 0; i < 4; i++) {
    tmp = w << 8 * i;
    bt[i] = (int)(tmp >> 24).to_ulong();
  }
  int i, j;
  for (int k = 0; k < 4; k++) {
    i = (bt[k] >> 4).to_ulong();
    j = ((bt[k] << 4) >> 4).to_ulong();
    bt[k] = Sbox[i][j];
  }
  for (int i = 0; i < 4; i++) {
    ans ^= (((Word)bt[i].to_ulong()) << 24 - 8 * i);
  }
  return (int)ans.to_ulong();
}
int RotWord(Word w) {
  Word high, low;
  high ^= w << 8;
  low ^= w >> 24;
  Word ans = high ^ low;
  return (int)ans.to_ulong();
}

void result(Byte**cipher,QString &ans){
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (cipher[i][j] == 0x00)
          ans.append("00");
        else if (cipher[i][j].to_ulong() > 0 && cipher[i][j].to_ulong() <= 15)
        {
            ans.append("0");
            ans+=QString::number(cipher[i][j].to_ulong(),16);
        }
        else {
          ans+=QString::number(cipher[i][j].to_ulong(),16);
        }
      }
    }
    ans = ans.toUpper();
}

//轮函数
void AddRoundKey(Byte** state, Word* RoundKey, int r) {
  Byte** rkey = new Byte*[4];
  for (int i = 0; i < 4; i++) {
    rkey[i] = new Byte[4];
  }
  for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 4; j++) {
      Word wt = RoundKey[i + 4 * r] << 8 * j;
      Byte bt = (wt >> 24).to_ulong();
      rkey[i][j] = bt;
    }
  }

  for (int i = 0; i < 4; i++)
    for (int j = 0; j < 4; j++) {
      state[i][j] = state[i][j] ^ rkey[i][j];
    }
}
void SubBytes(Byte** state) {
  for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 4; j++) {
      int x, y;
      x = (state[i][j] >> 4).to_ulong();
      y = ((state[i][j] << 4) >> 4).to_ulong();
      state[i][j] = Sbox[x][y];
    }
  }
}
void ShiftRows(Byte** state) {
  for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 4 - i; j++) {
      swap(state[i][j], state[i][j + i]);
    }
  }
  swap(state[3][1], state[3][3]);
  swap(state[3][2], state[3][3]);
}
//需要设置临时state
void MixColumns(Byte** state) {
  Byte tmp[4];
  Byte c[4][4] = {{0x02, 0x03, 0x01, 0x01},
                  {0x01, 0x02, 0x03, 0x01},
                  {0x01, 0x01, 0x02, 0x03},
                  {0x03, 0x01, 0x01, 0x02}};
  for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 4; j++) {
      for (int k = 0; k < 4; k++) {
        tmp[j] = tmp[j] ^ GFMul(c[j][k], state[k][i]);
      }
      // state[j][i] = tmp;不能直接赋值
    }
    for (int m = 0; m < 4; m++) {
      state[m][i] = tmp[m];
      tmp[m].reset();
    }
  }
}
void Round(Byte** state, Word* RoundKey, int r) {
    SubBytes(state);

    trans(state);
    ShiftRows(state);
    trans(state);

    trans(state);
    MixColumns(state);
    trans(state);

    AddRoundKey(state, RoundKey, r);
}

void InvSubBytes(Byte** state) {
  for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 4; j++) {
      int x, y;
      x = (state[i][j] >> 4).to_ulong();
      y = ((state[i][j] << 4) >> 4).to_ulong();
      state[i][j] = InvSbox[x][y];
    }
  }
}

void InvShiftRows(Byte** state) {
  for (int i = 0; i < 4; i++) {
    for (int j = 3; j >= 0 + i; j--) {
      swap(state[i][j], state[i][j - i]);
    }
  }
  swap(state[3][0], state[3][1]);
  swap(state[3][1], state[3][2]);
}

void InvMixColumns(Byte** state) {
  Byte tmp[4];
  Byte c[4][4] = {{0x0e, 0x0b, 0x0d, 0x09},
                  {0x09, 0x0e, 0x0b, 0x0d},
                  {0x0d, 0x09, 0x0e, 0x0b},
                  {0x0b, 0x0d, 0x09, 0x0e}};
  for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 4; j++) {
      for (int k = 0; k < 4; k++) {
        tmp[j] = tmp[j] ^ GFMul(c[j][k], state[k][i]);
      }
    }
    for (int m = 0; m < 4; m++) {
      state[m][i] = tmp[m];
      tmp[m].reset();
    }
  }
}

void InvRound(Byte** state, Word* RoundKey, int r) {
  trans(state);
  InvShiftRows(state);
  trans(state);

  InvSubBytes(state);

  AddRoundKey(state, RoundKey, r);

  trans(state);
  InvMixColumns(state);
  trans(state);
}
