#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QString>
#include <QPair>
#include <QPushButton>
#include <QMessageBox>
#include <bitset>
using namespace std;

typedef bitset<8> Byte;
typedef bitset<32> Word;

/* SPN */
const int l = 4;
const int m = 4;
const int Nr = 4;

const bitset<4> s_box[16] = {0b1110, 0b0100, 0b1101, 0b0001, 0b0010, 0b1111,
                             0b1011, 0b1000, 0b0011, 0b1010, 0b0110, 0b1100,
                             0b0101, 0b1001, 0b0000, 0b0111};

const bitset<4> inv_s_box[16] = {0b1110, 0b0011, 0b0100, 0b1000, 0b0001, 0b1100,
                                 0b1010, 0b1111, 0b0111, 0b1101, 0b1001, 0b0110,
                                 0b1011, 0b0010, 0b0000, 0b0101};

const int p_box[16][16] = {{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                           {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                           {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                           {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                           {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}};

const int inv_p_box[16][16] = {{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
                               {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                               {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                               {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                               {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}};

/* AES */
const int AESNr = 10;  //加密轮数-->取决于密钥大小
const int Nk = 4;   //种子密钥的列数
//轮常数
const Word r_con[10] = {0x01000000, 0x02000000, 0x04000000, 0x08000000, 0x10000000,
                        0x20000000, 0x40000000, 0x80000000, 0x1b000000, 0x36000000};
//加密S盒
const Byte Sbox[16][16] = {{0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5,
                            0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
                           {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0,
                            0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
                           {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc,
                            0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
                           {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a,
                            0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
                           {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0,
                            0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
                           {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b,
                            0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
                           {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85,
                            0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
                           {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5,
                            0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
                           {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17,
                            0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
                           {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88,
                            0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
                           {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c,
                            0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
                           {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9,
                            0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
                           {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6,
                            0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
                           {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e,
                            0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
                           {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94,
                            0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
                           {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68,
                            0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}};
//解密S盒
const Byte InvSbox[16][16] = {{0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38,
                               0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
                              {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87,
                               0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
                              {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d,
                               0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
                              {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2,
                               0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
                              {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16,
                               0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
                              {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda,
                               0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
                              {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a,
                               0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
                              {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02,
                               0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
                              {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea,
                               0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
                              {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85,
                               0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
                              {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89,
                               0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
                              {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20,
                               0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
                              {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31,
                               0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
                              {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d,
                               0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
                              {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0,
                               0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
                              {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26,
                               0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}};
/* 字符表 */
const char alphabet[28] = "abcdefghijklmnopqrstuvwxyz ";

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow;}
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

signals:
    void randomShow();
    void helpShow();

private slots:

    void on_ShiftCipherBtn_toggled(bool checked);

    void on_AffineCipherBtn_toggled(bool checked);

    void on_HillCipherBtn_toggled(bool checked);

    void on_VigenereCipherBtn_toggled(bool checked);

    void on_PermutationCipherBtn_toggled(bool checked);

    void on_SubstitutionCipherBtn_toggled(bool checked);

    void on_encryBtn_2_clicked();

    void on_decryBtn_2_clicked();

    void on_SPNBtn_toggled(bool checked);

    void on_AESBtn_toggled(bool checked);

    void on_randomBtn_clicked();

    void receiveMain();

    void on_actionUse_guide_triggered();

    void buttonClicked(QAbstractButton * butClicked);

private:
    Ui::MainWindow *ui;
};

class Crypto{
public:
    Crypto();
    ~Crypto();
    void setKey(QString k);
    QString getKey();

    /* 加解密部分 */
    // 1. 移位密码
    QString ShiftEncryption(QString input, int* cipher, int key, int len);
    QString ShiftDecryption(QString input, int* plain, int key, int len);
    // 2. 代换密码
    QString SubstitutionEncryption(QString input, int* cipher, int* key, int len);
    QString SubstitutionDecryption(QString input, int* plain, int* inversekey, int len);
    // 3. 仿射密码
    QString AffineEncryption(QString input, int* cipher, QPair<int, int> key, int len);
    QString AffineDecryption(QString input, int* plain, QPair<int, int> key, int len);
    // 4. 维吉尼亚密码
    QString VigenereEncryption(QString input, int* cipher, int m, int* key, int len);
    QString VigenereDecryption(QString input, int* plain, int m, int* key, int len);
    // 5. 希尔密码
    QString HillEncryption(QString input, int** cipher, int** key, int r, int m);
    QString HillDecryption(QString input, int** plain, int** inversekey, int r, int m);
    // 6. 置换密码
    QString PermutationEncryption(QString input, int** cipher, int* key, int r, int m);
    QString PermutationDecryption(QString input, int** plain, int* key, int r, int m);
    //7. SPN
    void KeyExpansion(bitset<4>* key, bitset<4>* RoundKey, int r);
    void SpnEncryption(bitset<4>* plain, bitset<4>* cipher, bitset<4>* key);
    void SpnDecryption(bitset<4>* cipher, bitset<4>* plain, bitset<4>* key);
    //8. AES
    void AESKeyExpansion(Byte** key, Word* RoundKey);
    void AesEncryption(Byte** plain, Byte** cipher, Byte** key);
    void AesDecryption(Byte** cipher, Byte** plain, Byte** key);

private:
    QString key;
};
#endif // MAINWINDOW_H