#include "crypto.h"
#include "function.cpp"
#include <ui_MainWindow.h>
#include <QFile>
#include <QDebug>
#include <QMessageBox>
#include <QByteArray>
#include <QCloseEvent>
#include <iostream>
#include <iomanip>
#include <pseudorandom.h>
QPushButton*okBtn;

using namespace std;

const QString intro = "欢迎使用密码工具箱。\n "
                      "密码学简介:\n"
                      "密码学是研究编制密码和破译密码的技术科学。研究密码变化的客观规律，"
                      "应用于编制密码以保守通信秘密的，称为编码学；应用于破译密码以获取通信情报的，称为破译学，总称密码学。"
                      "现代信息安全的基本要求:信息的保密性 Confidentiality：防止信息泄漏给未经授权的人（加密解密技术）\n 信息的完整性 Integrity：防止信息被未经授权的篡改（消息认证码，数字签名）"
                      "认证性 Authentication：保证信息来自正确的发送者（消息认证码，数字签名）"
                      "不可否认性 Non-repudiation：保证发送者不能否认他们已发送的消息（数字签名）\n"
                      "希望大家能够通过此工具箱更好的学习密码学。";

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    ui->introduction->setText(intro);
    ui->keyInput->setPlaceholderText("此处输入符合规定的密钥");
    ui->InputText->setPlaceholderText("此处输入明文或者密文");
    ui->OutputText->setPlaceholderText("输出对应的加密结果或解密结果");
}

MainWindow::~MainWindow()
{
    delete ui;
}

Crypto::Crypto(){}
Crypto::~Crypto(){};
void Crypto::setKey(QString k){key = k;}
QString Crypto::getKey(){return key;}


//加解密算法实现
QString Crypto::HillEncryption(QString input, int** cipher, int** key,
                               int r, int m) {
  if (input.size() % m != 0) {
    int size = m - input.size() % m;
    for (int i = 0; i < size; i++) input += ' ';
  }
  int** plain = new int*[r];
  for (int i = 0; i < r; i++) plain[i] = new int[m];
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  for (int i = 0; i < r; i++)
    for (int j = 0; j < m; j++) plain[i][j] = converse(Input[i * m + j]);
  multi(plain, key, cipher, r, m);
  QString output;
  for (int i = 0; i < r; i++)
    for (int j = 0; j < m; j++) {
      output.append(alphabet[cipher[i][j]]);
    }
  for (int i = 0; i < r; i++) delete[] plain[i];
  delete[] plain;
  return output;
}
QString Crypto::HillDecryption(QString input, int** plain, int** inversekey,
                               int r, int m) {
  int** cipher = new int*[r];
  for (int i = 0; i < r; i++) cipher[i] = new int[m];
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  for (int i = 0; i < r; i++)
  for (int j = 0; j < m; j++) cipher[i][j] = converse(Input[i * m + j]);
  multi(cipher, inversekey, plain, r, m);
  QString output;
  for (int i = 0; i < r; i++)
    for (int j = 0; j < m; j++) output.append(alphabet[plain[i][j]]);
  return output;
}
QString Crypto::ShiftEncryption(QString input, int* cipher, int key,
                                int len) {
  int* plain = new int[len];
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  QString output;
  for (int i = 0; i < len; i++) plain[i] = converse(Input[i]);
  for (int i = 0; i < len; i++) cipher[i] = (plain[i] + key) % 27;
  for (int i = 0; i < len; i++) output.append(alphabet[cipher[i]]);
  delete[] plain;
  return output;
}
QString Crypto::ShiftDecryption(QString input, int* plain, int key, int len) {
  int* cipher = new int[len];
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  QString output;
  for (int i = 0; i < len; i++) cipher[i] = converse(Input[i]);
  for (int i = 0; i < len; i++) plain[i] = ((cipher[i] - key) % 27 + 27) % 27;
  for (int i = 0; i < len; i++) output.append(alphabet[plain[i]]);
  return output;
}
QString Crypto::SubstitutionEncryption(QString input, int* cipher, int* key,
                                       int len) {
  int* plain = new int[len];
  QString output;
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  for (int i = 0; i < len; i++) plain[i] = converse(Input[i]);
  for (int i = 0; i < len; i++) cipher[i] = key[plain[i]];
  for (int i = 0; i < len; i++) output.append(alphabet[cipher[i]]);
  delete[] plain;
  return output;
}
QString Crypto::SubstitutionDecryption(QString input, int* plain,
                                       int* inverseKey, int len) {
  int* cipher = new int[len];
  QString output;
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  for (int i = 0; i < len; i++) cipher[i] = converse(Input[i]);
  for (int i = 0; i < len; i++) plain[i] = inverseKey[cipher[i]];
  for (int i = 0; i < len; i++) output.append(alphabet[plain[i]]);
  return output;
}
QString Crypto::AffineEncryption(QString input, int* cipher,
                                 QPair<int, int> key, int len) {
  int* plain = new int[len];
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  QString output;
  for (int i = 0; i < len; i++) plain[i] = converse(Input[i]);
  for (int i = 0; i < len; i++)
    cipher[i] = (key.first * plain[i] + key.second) % 27;
  for (int i = 0; i < len; i++) output.append(alphabet[cipher[i]]);
  delete[] plain;
  return output;
}
QString Crypto::AffineDecryption(QString input, int* plain,
                                 QPair<int, int> key, int len) {
  int a[18] = {1,  2,  4,  5,  7,  8,  10, 11, 13,
               14, 16, 17, 19, 20, 22, 23, 25, 26};
  int aInv[18] = {1, 14, 7, 11, 4,  17, 19, 5,  25,
                  2, 22, 8, 10, 23, 16, 20, 13, 26};
  int index, a_inverse;
  int* cipher = new int[len];
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  QString output;
  for (int i = 0; i < len; i++) cipher[i] = converse(Input[i]);
  for (int i = 0; i < 18; i++)
    if (key.first == a[i]) index = i;
  a_inverse = aInv[index];
  for (int i = 0; i < len; i++)
    plain[i] = ((a_inverse * (cipher[i] - key.second)) % 27 + 27) % 27;
  for (int i = 0; i < len; i++) output.append(alphabet[plain[i]]);
  return output;
}
QString Crypto::VigenereEncryption(QString input, int* cipher, int m,
                                   int* key, int len) {
  int* plain = new int[len];
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  QString output;
  for (int i = 0; i < len; i++) plain[i] = converse(Input[i]);
  int i;
  for (i = 0; i + m < len; i += m)
    for (int j = 0; j < m; j++) cipher[i + j] = (plain[i + j] + key[j]) % 27;
  int index = 0;
  for (i = i; i < len; i++) cipher[i] = (plain[i] + key[index++]) % 27;
  for (int i = 0; i < len; i++) output.append(alphabet[cipher[i]]);
  delete[] plain;
  return output;
}
QString Crypto::VigenereDecryption(QString input, int* plain, int m, int* key,
                                   int len) {
  int i;
  int* cipher = new int[len];
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  QString output;
  for (int i = 0; i < len; i++) cipher[i] = converse(Input[i]);
  for (i = 0; i + m < len; i += m) {
    for (int j = 0; j < m; j++) {
      plain[i + j] = ((cipher[i + j] - key[j]) % 27 + 27) % 27;
    }
  }
  int index = 0;
  for (i = i; i < len; i++)
    plain[i] = ((cipher[i] - key[index++]) % 27 + 27) % 27;
  for (int i = 0; i < len; i++) output.append(alphabet[plain[i]]);
  return output;
}
QString Crypto::PermutationEncryption(QString input, int** cipher, int* key,
                                  int r, int m) {
  if (input.size() % m != 0) {
    int size = m - input.size() % m;
    for (int i = 0; i < size; i++) input += 'a';
  }
  int** plain = new int*[r];
  for (int i = 0; i < r; i++) plain[i] = new int[m];
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  for (int i = 0; i < r; i++)
    for (int j = 0; j < m; j++) plain[i][j] = converse(Input[i * m + j]);
  int** permutation = new int*[m];
  for (int i = 0; i < m; i++) permutation[i] = new int[m];
  for (int i = 0; i < m; i++)
    for (int j = 0; j < m; j++) permutation[i][j] = 0;
  for (int i = 0; i < m; i++) permutation[key[i]][i] = 1;
  multi(plain, permutation, cipher, r, m);
  QString output;
  for (int i = 0; i < r; i++)
    for (int j = 0; j < m; j++) output.append(alphabet[cipher[i][j]]);
  for (int i = 0; i < r; i++) delete[] plain[i];
  delete[] plain;
  for (int i = 0; i < m; i++) delete[] permutation[i];
  delete[] permutation;
  return output;
}
QString Crypto::PermutationDecryption(QString input, int** plain, int* key,
                                  int r, int m) {
  int** cipher = new int*[r];
  for (int i = 0; i < r; i++) cipher[i] = new int[m];
  QByteArray tmp = input.toLatin1();
  char* Input = tmp.data();
  for (int i = 0; i < r; i++)
  for (int j = 0; j < m; j++) cipher[i][j] = converse(Input[i * m + j]);
  int** inverse = new int*[m];
    for (int i = 0; i < m; i++) inverse[i] = new int[m];
  for (int i = 0; i < m; i++)
    for (int j = 0; j < m; j++) inverse[i][j] = 0;
  for (int i = 0; i < m; i++) inverse[i][key[i]] = 1;
  multi(cipher, inverse, plain, r, m);
  QString output;
  for (int i = 0; i < r; i++)
    for (int j = 0; j < m; j++) output.append(alphabet[plain[i][j]]);
  for (int i = 0; i < m; i++) delete[] inverse[i];
  delete[] inverse;
  return output;
}
void Crypto::KeyExpansion(bitset<4> *key, bitset<4> *RoundKey, int r){
    for (int i = r - 1; i < r + 3; i++) RoundKey[i - r + 1] = key[i];
}
void print(bitset<4>* bs, int len){
    string tmp;
    for (int i = 0; i < len; i++) {
        tmp.append(bs[i].to_string());
    }
}
void  Crypto::SpnEncryption(bitset<4> *plain, bitset<4> *cipher, bitset<4> *key){
  bitset<4>* RoundKey = new bitset<4>[m];
  bitset<4>* w = new bitset<4>[m];
  bitset<4>* u = new bitset<4>[m];
  bitset<4>* v = new bitset<4>[m];
  int* arr = new int[16];
  int* ans = new int[16];
  for (int i = 0; i < m; i++) {
      w[i] = plain[i];
  }

  for (int r = 0; r < Nr - 1; r++) {
    KeyExpansion(key, RoundKey, r + 1);
    for (int i = 0; i < m; i++) u[i] = w[i] ^ RoundKey[i];
    for (int i = 0; i < m; i++) v[i] = s_box[u[i].to_ulong()];
    toInt(v, arr);
    spnMulti(arr, p_box, ans);
    string temp;
    for (int i = 0; i < 16; i++) temp.push_back((char)('0' + ans[i]));
    toBitset(temp, w);
  }
  KeyExpansion(key, RoundKey, Nr);
  for (int i = 0; i < m; i++) u[i] = w[i] ^ RoundKey[i];
  for (int i = 0; i < m; i++) v[i] = s_box[u[i].to_ulong()];
  KeyExpansion(key, RoundKey, Nr + 1);
  for (int i = 0; i < m; i++) cipher[i] = v[i] ^ RoundKey[i];
}
void Crypto:: SpnDecryption(bitset<4>* cipher, bitset<4>* plain, bitset<4>* key) {
  bitset<4>* RoundKey = new bitset<4>[m];
  bitset<4>* w = new bitset<4>[m];
  bitset<4>* u = new bitset<4>[m];
  bitset<4>* v = new bitset<4>[m];
  int* arr = new int[16];
  int* ans = new int[16];
  KeyExpansion(key, RoundKey, Nr + 1);
  for (int i = 0; i < m; i++) v[i] = cipher[i] ^ RoundKey[i];
  for (int i = 0; i < m; i++) u[i] = inv_s_box[v[i].to_ulong()];
  KeyExpansion(key, RoundKey, Nr);
  for (int i = 0; i < m; i++) w[i] = u[i] ^ RoundKey[i];
  for (int r = Nr - 2; r >= 0; r--) {
    toInt(w, arr);
    spnMulti(arr, inv_p_box, ans);
    string temp;
    for (int i = 0; i < 16; i++) temp.push_back((char)('0' + ans[i]));
    toBitset(temp, v);
    for (int i = 0; i < m; i++) u[i] = inv_s_box[v[i].to_ulong()];
    KeyExpansion(key, RoundKey, r + 1);
    for (int i = 0; i < m; i++) w[i] = u[i] ^ RoundKey[i];
  }
  for (int i = 0; i < m; i++) plain[i] = w[i];
}

void Crypto::AESKeyExpansion(Byte** key, Word* RoundKey) {
  for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 4; j++)
      RoundKey[i] ^= ((Word)key[i][j].to_ulong() << 24 - 8 * j);
  }
  for (int i = 4; i < 44; i++) {
    Word t1, t2;
    Word tmp = RoundKey[i - 1];
    if (i % 4 == 0) {
      t1 = (RotWord(tmp));
      t2 = (SubWord(t1));
      tmp = t2 ^ r_con[(i / 4) - 1];
    }
    RoundKey[i] = RoundKey[i - 4] ^ tmp;
  }
}

void Crypto::AesEncryption(Byte** plain, Byte** cipher, Byte** key) {
    Byte**state = new Byte*[4];
    for(int i=0;i<4;i++)
        state[i] = new Byte[4];
    for(int i=0;i<4;i++)
        for(int j=0;j<4;j++)
            state[i][j] = plain[i][j];
    Word* RoundKey = new Word[44];

    AESKeyExpansion(key, RoundKey);
    AddRoundKey(state, RoundKey, 0);

    for (int i = 0; i < 9; i++) {
      Round(state, RoundKey, i + 1);
    }

    trans(state);
    SubBytes(state);
    trans(state);
    trans(state);
    ShiftRows(state);
    trans(state);

    AddRoundKey(state, RoundKey, 10);

    for(int i=0;i<4;i++)
        for(int j=0;j<4;j++)
            cipher[i][j] = state[i][j];

    for(int i=0;i<4;i++)
        delete[] state[i];
    delete[]state;
    delete[]RoundKey;
}

void Crypto::AesDecryption(Byte** cipher, Byte** plain, Byte** key) {
  Word* RoundKey = new Word[44];
  Byte** state = new Byte*[4];
  for (int i = 0; i < 4; i++) {
    state[i] = new Byte[4];
  }
  for (int i = 0; i < 4; i++)
    for (int j = 0; j < 4; j++) {
      state[i][j] = cipher[i][j];
    }

  AESKeyExpansion(key, RoundKey);
  AddRoundKey(state, RoundKey, 10);

  for (int i = 8; i >= 0; i--) {
    InvRound(state, RoundKey, i + 1);
  }

  trans(state);
  InvShiftRows(state);
  trans(state);

  InvSubBytes(state);

  AddRoundKey(state, RoundKey, 0);

  for (int i = 0; i < 4; i++)
    for (int j = 0; j < 4; j++) plain[i][j] = state[i][j];

  for(int i=0;i<4;i++)
      delete[] state[i];
  delete[]state;
  delete[]RoundKey;
}

/* 信号与槽 */
void MainWindow::on_randomBtn_clicked()
{
    this->hide();
    emit randomShow();
}
void MainWindow::receiveMain()
{
    this->show();
}

void MainWindow::on_actionUse_guide_triggered()
{
    emit helpShow();
}

void MainWindow::buttonClicked(QAbstractButton * butClicked){
    if(butClicked == (QAbstractButton*)okBtn)
        this->close();
}

void MainWindow::on_ShiftCipherBtn_toggled(bool checked) {
  checked = ui->ShiftCipherBtn->isChecked();
  if (checked) {
    ui->keyInput->clear();
    ui->InputText->clear();
    ui->OutputText->clear();
    ui->keyInput->setPlaceholderText("0 ~ 26");
    QFile file("E:/Crypto/ToolsCrypt/introduction/ShiftCipher.txt");
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
      qDebug() << "Can't open the file!" << endl;
    }
    QString intro;
    while (!file.atEnd()) {
      QByteArray text = file.readLine();
      intro.append(text);
      intro.append('\n');
    }
    ui->introduction->setReadOnly(true);
    ui->introduction->setText(intro);
  }
}

void MainWindow::on_AffineCipherBtn_toggled(bool checked) {
  checked = ui->AffineCipherBtn->isChecked();
  if (checked) {
    ui->keyInput->clear();
    ui->InputText->clear();
    ui->OutputText->clear();
    ui->keyInput->setPlaceholderText("a=1,2,4,5,7,8,11,13,14,16,17,19,20,22,23,25,26; b=0~26");
    QFile file("E:/Crypto/ToolsCrypt/introduction/AffineCipher.txt");
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
      qDebug() << "Can't open the file!" << endl;
    }
    QString intro;
    while (!file.atEnd()) {
      QByteArray text = file.readLine();
      intro.append(text);
      intro.append('\n');
    }
    ui->introduction->setReadOnly(true);
    ui->introduction->setText(intro);
  }
}

void MainWindow::on_HillCipherBtn_toggled(bool checked) {
  checked = ui->HillCipherBtn->isChecked();
  if (checked) {
    ui->keyInput->clear();
    ui->InputText->clear();
    ui->OutputText->clear();
    ui->keyInput->setPlaceholderText("m*m的可逆矩阵");
    QFile file("E:/Crypto/ToolsCrypt/introduction/HillCipher.txt");
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
      qDebug() << "Can't open the file!" << endl;
    }
    QString intro;
    while (!file.atEnd()) {
      QByteArray text = file.readLine();
      intro.append(text);
      intro.append('\n');
    }
    ui->introduction->setReadOnly(true);
    ui->introduction->setText(intro);
  }
}

void MainWindow::on_VigenereCipherBtn_toggled(bool checked) {
  checked = ui->VigenereCipherBtn->isChecked();
  if (checked) {
    ui->keyInput->clear();
    ui->InputText->clear();
    ui->OutputText->clear();
    ui->keyInput->setPlaceholderText("k1...km(0 ~ 26)2 8 15 7 4 17");
    QFile file("E:/Crypto/ToolsCrypt/introduction/VigenereCipher.txt");
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
      qDebug() << "Can't open the file!" << endl;
    }
    QString intro;
    while (!file.atEnd()) {
      QByteArray text = file.readLine();
      intro.append(text);
      intro.append('\n');
    }
    ui->introduction->setReadOnly(true);
    ui->introduction->setText(intro);
  }
}

void MainWindow::on_PermutationCipherBtn_toggled(bool checked) {
  checked = ui->PermutationCipherBtn->isChecked();
  if (checked) {
    ui->keyInput->clear();
    ui->InputText->clear();
    ui->OutputText->clear();
    ui->keyInput->setPlaceholderText("(0 ~ m-1 的置换)2 4 0 5 3 1");
    QFile file("E:/Crypto/ToolsCrypt/introduction/PermutationCipher.txt");
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
      qDebug() << "Can't open the file!" << endl;
    }
    QString intro;
    while (!file.atEnd()) {
      QByteArray text = file.readLine();
      intro.append(text);
      intro.append('\n');
    }
    ui->introduction->setReadOnly(true);
    ui->introduction->setText(intro);
  }
}

void MainWindow::on_SubstitutionCipherBtn_toggled(bool checked) {
  checked = ui->SubstitutionCipherBtn->isChecked();
  if (checked) {
    ui->keyInput->clear();
    ui->InputText->clear();
    ui->OutputText->clear();
    ui->keyInput->setPlaceholderText("0 ~ 26的置换 23 13 24 0 7 15 14 6 25 16 22 1 19 18 5 11 17 2 21 12 20 4 10 9 3 26 8");
    QFile file("E:/Crypto/ToolsCrypt/introduction/SubstitutionCipher.txt");
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
      qDebug() << "Can't open the file!" << endl;
    }
    QString intro;
    while (!file.atEnd()) {
      QByteArray text = file.readLine();
      intro.append(text);
      intro.append('\n');
    }
    ui->introduction->setReadOnly(true);
    ui->introduction->setText(intro);
  }
}


void MainWindow::on_SPNBtn_toggled(bool checked){
    checked = ui->SPNBtn->isChecked();
      if (checked) {
        ui->keyInput->clear();
        ui->InputText->clear();
        ui->OutputText->clear();
        ui->keyInput->setPlaceholderText("32位二元密钥，00111010100101001101011000111111");
        ui->InputText->setPlaceholderText("输入二元消息字符串,大小为16比特");
        QFile file("E:/Crypto/ToolsCrypt/introduction/SPN.txt");
        if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
          qDebug() << "Can't open the file!" << endl;
        }
        QString intro;
        while (!file.atEnd()) {
          QByteArray text = file.readLine();
          intro.append(text);
          intro.append('\n');
        }
        ui->introduction->setReadOnly(true);
        ui->introduction->setText(intro);
      }
}

void MainWindow::on_AESBtn_toggled(bool checked){
    checked = ui->AESBtn->isChecked();
      if (checked) {
        ui->keyInput->clear();
        ui->InputText->clear();
        ui->OutputText->clear();
        ui->keyInput->setPlaceholderText("128位密钥，输入32个十六进制数");
        ui->InputText->setPlaceholderText("输入32个十六进制数作为消息串");
        QFile file("E:/Crypto/ToolsCrypt/introduction/AES.txt");
        if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
          qDebug() << "Can't open the file!" << endl;
        }
        QString intro;
        while (!file.atEnd()) {
          QByteArray text = file.readLine();
          intro.append(text);
          intro.append('\n');
        }
        ui->introduction->setReadOnly(true);
        ui->introduction->setText(intro);
      }
}


//加密
void MainWindow::on_encryBtn_2_clicked() {
  Crypto crypto;
  QString input = ui->InputText->toPlainText();
  int len = input.size();
  crypto.setKey(ui->keyInput->text());

  //移位密码加密
  if (ui->ShiftCipherBtn->isChecked()) {
    int Key = crypto.getKey().toInt();
    int* cipher = new int[len];
    QString output = crypto.ShiftEncryption(input, cipher, Key, len);
    ui->OutputText->setText(output);
  }
  //置换加密
  else if (ui->PermutationCipherBtn->isChecked()) {
    if (!ui->InputText->document()->isEmpty()) {
      ui->InputText->clear();
    }
    if (!ui->OutputText->document()->isEmpty()) {
      ui->OutputText->clear();
    }
    QStringList keyList = crypto.getKey().split(" ");
    int size = keyList.size();
    int* Key = new int[size];
    for (int i = 0; i < size; i++) {
      Key[i] = keyList[i].toInt();
    }
    int r;
    int m = (ui->m->text()).toInt();
    if (input.size() % m == 0)
      r = input.size() / m;
    else
      r = (input.size() / m) + 1;
    int** cipher = new int*[r];
    for (int i = 0; i < r; i++) cipher[i] = new int[m];
    QString output = crypto.PermutationEncryption(input,cipher,Key,r,m);
    ui->OutputText->setText(output);
    for (int i = 0; i < r; i++) delete[] cipher[i];
    delete[] cipher;
    delete[] Key;
  }
  //希尔密码加密
  else if (ui->HillCipherBtn->isChecked()) {
      int r, m;
      m = (ui->m->text()).toInt();
      int** Key = new int*[m];
      int** inverseKey = new int*[m];
      for (int i = 0; i < m; i++) {
        Key[i] = new int[m];
        inverseKey[i] = new int[m];
      }
      QStringList keyList = crypto.getKey().split(" ");
      for (int i = 0; i < m; i++)
        for (int j = 0; j < m; j++) Key[i][j] = keyList[m * i + j].toInt();
      bool flag = inverse(Key, inverseKey, m);
      if (!flag) {
          QMessageBox error(QMessageBox::Question,"","");
          error.setParent(this);
          error.setWindowFlag(Qt::Dialog);
          error.setWindowTitle("警告");
          error.setText("输入密钥错误，不是环中的可逆矩阵，可参照简介输入。");
          error.show();
          QObject::connect(&error,&QMessageBox::buttonClicked,this,&MainWindow::buttonClicked);
          error.exec();
          for (int i = 0; i < m; i++) {
            delete[] Key[i];
            delete[]inverseKey[i];
          }
          delete[] Key;
          delete[]inverseKey;
      }
      else{
          if (input.size() % m == 0)
              r = input.size() / m;
          else
              r = (input.size() / m) + 1;
          int** cipher = new int*[r];
          for (int i = 0; i < r; i++) {
              cipher[i] = new int[m];
          }
          QString output = crypto.HillEncryption(input,cipher,Key,r,m);
          ui->OutputText->setText(output);
          for (int i = 0; i < m; i++) {
              delete[] Key[i];
              delete[]inverseKey[i];
          }
          for (int i = 0; i < r; i++) {
              delete[] cipher[i];
          }
          delete[] Key;
          delete[] cipher;
          delete[] inverseKey;
          }
  }
  //仿射密码加密
  else if (ui->AffineCipherBtn->isChecked()) {
    QPair<int, int> Key;
    QStringList keyList = crypto.getKey().split(" ");
    Key.first = keyList[0].toInt();
    Key.second = keyList[1].toInt();
    int len = input.size();
    int* cipher = new int[len];
    QString output =crypto.AffineEncryption(input, cipher, Key, len);
    ui->OutputText->setText(output);
    delete[] cipher;
  }
  //代换密码加密
  else if (ui->SubstitutionCipherBtn->isChecked()) {
    int Key[27];
    int len = input.size();
    QStringList keyList = crypto.getKey().split(" ");
    for (int i = 0; i < 27; i++) Key[i] = keyList[i].toInt();
    int* cipher = new int[len];
    QString output =crypto.SubstitutionEncryption(input, cipher, Key, len);
    ui->OutputText->setText(output);
    delete[] cipher;
  }
  //维吉尼亚密码加密
  else if (ui->VigenereCipherBtn->isChecked()) {
    int m = (ui->m->text()).toInt();
    int* Key = new int[m];
    QStringList keyList = crypto.getKey().split(" ");
    for (int i = 0; i < m; i++) Key[i] = keyList[i].toInt();
    int len = input.size();
    int* cipher = new int[len];
    QString output =crypto.VigenereEncryption(input, cipher, m, Key, len);
    ui->OutputText->setText(output);
    delete[] Key;
    delete[] cipher;
  }
  //SPN加密
  else if(ui->SPNBtn->isChecked()){
    QString output;
    bitset<4>* plain = new bitset<4>[m];
    bitset<4>* cipher = new bitset<4>[m];
    bitset<4>* key = new bitset<4>[2 * m];
    string in = input.toStdString();
    toBitset(in, plain);
    QString Key = crypto.getKey();
    string k = Key.toStdString();
    toBitset(k, key);
    crypto.SpnEncryption(plain, cipher, key);
    for (int i = 0; i < 4; i++) {
        string tmp = cipher[i].to_string();
        output += QString::fromStdString(tmp);
    }
   ui->OutputText->setText(output);
  }
  //AES加密
  else if(ui->AESBtn->isChecked()){
      QString ans;
      Byte** Key = new Byte*[4];
      Byte** Plain = new Byte*[4];
      Byte** cipher = new Byte*[4];
      for (int i = 0; i < 4; i++) {
        Key[i] = new Byte[4];
        Plain[i] = new Byte[4];
        cipher[i] = new Byte[4];
      }
      string key =crypto.getKey().toStdString();
      string in = input.toStdString();
      toByte(key, Key);
      toByte(in, Plain);

      crypto.AesEncryption(Plain, cipher, Key);

      result(cipher,ans);
      ui->OutputText->setText(ans);
  }
}

void MainWindow::on_decryBtn_2_clicked()
{
    Crypto crypto;
    QString input = ui->InputText->toPlainText();
    int len = input.size();
    crypto.setKey(ui->keyInput->text());

//移位密码解密
if (ui->ShiftCipherBtn->isChecked()) {
      int Key = crypto.getKey().toInt();
      int* plain = new int[len];
      QString output = crypto.ShiftDecryption(input, plain, Key, len);
      ui->OutputText->setText(output);
      delete[]plain;
}
//置换密码解密
else if (ui->PermutationCipherBtn->isChecked()) {
      if (!ui->InputText->document()->isEmpty()) {
        ui->InputText->clear();
      }
      if (!ui->OutputText->document()->isEmpty()) {
        ui->OutputText->clear();
      }
      QStringList keyList = crypto.getKey().split(" ");
      int size = keyList.size();
      int* Key = new int[size];
      for (int i = 0; i < size; i++) {
        Key[i] = keyList[i].toInt();
      }
      int r;
      int m = (ui->m->text()).toInt();
      if (input.size() % m == 0)
        r = input.size() / m;
      else
        r = (input.size() / m) + 1;
      int** plain = new int*[r];
      for (int i = 0; i < r; i++) plain[i] = new int[m];
      QString output = crypto.PermutationDecryption(input,plain,Key,r,m);
      ui->OutputText->setText(output);
      for (int i = 0; i < r; i++) delete[] plain[i];
      delete[] plain;
      delete[] Key;
}
//希尔密码解密
else if (ui->HillCipherBtn->isChecked()) {
        int r, m;
        m = (ui->m->text()).toInt();
        int** Key = new int*[m];
        int** inverseKey = new int*[m];
        for (int i = 0; i < m; i++) {
          Key[i] = new int[m];
          inverseKey[i] = new int[m];
        }
        QStringList keyList = crypto.getKey().split(" ");
        for (int i = 0; i < m; i++)
          for (int j = 0; j < m; j++) Key[i][j] = keyList[m * i + j].toInt();
        bool flag = inverse(Key, inverseKey, m);
        if (!flag) {
          QMessageBox error(QMessageBox::Question,"","");
          error.setParent(this);
          error.setWindowFlag(Qt::Dialog);
          error.setWindowTitle("警告");
          error.setText("输入密钥错误，不是环中的可逆矩阵，可参照简介输入。");
          error.show();
          QObject::connect(&error,&QMessageBox::buttonClicked,this,&MainWindow::buttonClicked);
          error.exec();
          for (int i = 0; i < m; i++) {
            delete[] Key[i];
            delete[] inverseKey[i];
          }
          delete[] Key;
          delete[] inverseKey;
        }
        else
        {
            if (input.size() % m == 0)
                r = input.size() / m;
            else
                r = (input.size() / m) + 1;
            int** plain = new int*[r];
            for (int i = 0; i < r; i++) {
                plain[i] = new int[m];
            }
            QString output = crypto.HillDecryption(input,plain,Key,r,m);
            ui->OutputText->setText(output);
            for (int i = 0; i < m; i++) {
                delete[] Key[i];
                delete[] inverseKey[i];
            }
            for (int i = 0; i < r; i++) {
                delete[] plain[i];
            }
            delete[] Key;
            delete[] plain;
            delete[] inverseKey;
        }
}
//仿射密码解密
else if (ui->AffineCipherBtn->isChecked()) {
  QPair<int, int> Key;
  QStringList keyList = crypto.getKey().split(" ");
  Key.first = keyList[0].toInt();
  Key.second = keyList[1].toInt();
  int len = input.size();
  int* plain = new int[len];
  QString output =crypto.AffineDecryption(input, plain, Key, len);
  ui->OutputText->setText(output);
  delete[] plain;
}
//代换密码解密
else if (ui->SubstitutionCipherBtn->isChecked()) {
  int Key[27];
  int len = input.size();
  QStringList keyList = crypto.getKey().split(" ");
  for (int i = 0; i < 27; i++) Key[i] = keyList[i].toInt();
  int* plain = new int[len];
  QString output =crypto.SubstitutionDecryption(input, plain, Key, len);
  ui->OutputText->setText(output);
  delete[] plain;
}
//维吉尼亚密码解密
else if (ui->VigenereCipherBtn->isChecked()) {
  int m = (ui->m->text()).toInt();
  int* Key = new int[m];
  QStringList keyList = crypto.getKey().split(" ");
  for (int i = 0; i < m; i++) Key[i] = keyList[i].toInt();
  int len = input.size();
  int* plain = new int[len];
  QString output =crypto.VigenereDecryption(input, plain, m, Key, len);
  ui->OutputText->setText(output);
  delete[] Key;
  delete[] plain;
}
//SPN解密
else if(ui->SPNBtn->isChecked()){
    QString output;
    bitset<4>* plain = new bitset<4>[m];
    bitset<4>* cipher = new bitset<4>[m];
    bitset<4>* key = new bitset<4>[2 * m];
    string in = input.toStdString();
    toBitset(in, cipher);
    QString Key = crypto.getKey();
    string k = Key.toStdString();
    toBitset(k, key);
    crypto.SpnDecryption(cipher, plain, key);
    for (int i = 0; i < 4; i++) {
        string tmp = plain[i].to_string();
        output += QString::fromStdString(tmp);
    }
   ui->OutputText->setText(output);
  }
//AES解密
else if(ui->AESBtn->isChecked()){
    QString ans;
    Byte** Key = new Byte*[4];
    Byte** Plain = new Byte*[4];
    Byte** cipher = new Byte*[4];
    for (int i = 0; i < 4; i++) {
      Key[i] = new Byte[4];
      Plain[i] = new Byte[4];
      cipher[i] = new Byte[4];
    }
    string in = input.toStdString();
    cout<<"in: "<<in<<endl;
    toByte(in, cipher);
    QString key = crypto.getKey();
    string k = key.toStdString();
    toByte(k, Key);
    crypto.AesDecryption(cipher, Plain, Key);

    result(Plain,ans);
    ui->OutputText->setText(ans);
}
}
