#ifndef PSEUDORANDOM_H
#define PSEUDORANDOM_H

#include <QWidget>

const int W = 64;
const int N = 312;
const int M = 156;
const int R = 31;
const uint64_t A = 0xB5026F5AA96619E9;
const uint64_t UPPER_MASK = 0xFFFFFFFF80000000;  // lowest w bits of(not LOWER_MASK)
const uint64_t LOWER_MASK = 0x7FFFFFFF;  //(1<<R)-1<--> 2^31. decimal：2147483647
const uint64_t F = 6364136223846793005;  // The constant f forms another
                                         // parameter to the generator.
const uint64_t D = 0x5555555555555555;
const uint64_t B = 0x71D67FFFEDA60000;
const uint64_t C = 0xFFF7EEE000000000;

namespace Ui {
class PseudoRandom;
}

class PseudoRandom : public QWidget
{
    Q_OBJECT

public:
    explicit PseudoRandom(QWidget *parent = nullptr);
    ~PseudoRandom();
    // Func
    void srand_init(uint64_t seed);  //获得基础的梅森旋转链
    void twist();             //对旋转链进行旋转算法
    uint64_t MersenneRand();  //获取随机数

signals:
    void mainShow();

private slots:
    void on_classicalBtn_clicked();
    void receiveRandom();

    void on_createBtn_clicked();

    void on_MensenBtn_clicked(bool checked);

    void on_clearBtn_clicked();

private:
    Ui::PseudoRandom *ui;
};

#endif // PSEUDORANDOM_H
