#include "pseudorandom.h"
#include "ui_pseudorandom.h"
#include <QFile>
#include <QDebug>

//全局变量
uint64_t mt[N];
size_t mti = N + 1;
bool flag;

PseudoRandom::PseudoRandom(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::PseudoRandom)
{
    ui->setupUi(this);
    ui->result->setPlaceholderText("输出随机数");
    ui->numberInput->setPlaceholderText("输入生成个数");
    ui->introduction->setPlaceholderText("伪随机数发生器简介");
}

PseudoRandom::~PseudoRandom()
{
    delete ui;
}

void PseudoRandom::srand_init(uint64_t seed){
    mti = 0;
    flag = true;
    mt[0] = seed;
    mt[0] = (seed >> 32) ^ mt[0];
    for (size_t i = 1; i < N; i++) {
      mt[i] = F * (mt[i - 1] ^ (mt[i - 1] >> (W - 2))) + i;
      mt[i] = mt[i] * D;
    }
}

void PseudoRandom::twist(){
    uint64_t x;
    for (size_t i = 0; i < N; i++) {
      x = (mt[i] & UPPER_MASK) + ((mt[i + 1] % N) & LOWER_MASK);
      mt[i] = mt[(i + M) % N] ^ (x >> 1);
      if (x & 1) mt[i] ^= A;
    }
}

uint64_t PseudoRandom::MersenneRand(){
    uint64_t y;
    if (!flag) {
      srand_init((uint64_t)time(NULL));
    }
    if (mti == 0) twist();
    y = mt[mti];
    y = y ^ ((y >> 29) & D);
    y = y ^ ((y >> 17) & B);
    y = y ^ ((y >> 37) & C);
    y = y ^ ((y >> 43));
    mti = (mti + 1) % N;
    return y;
}
void PseudoRandom::receiveRandom()
{
    this->show();
}

void PseudoRandom::on_classicalBtn_clicked()
{
    this->hide();
    emit mainShow();
}


void PseudoRandom::on_createBtn_clicked()
{
    PseudoRandom pr;
    int number = ui->numberInput->text().toInt();
    QString output;
    for(int i=0;i<number;i++){
        output.append(QString::number(pr.MersenneRand(),10));
        output+="\n";
    }
    ui->result->setText(output);
}

void PseudoRandom::on_MensenBtn_clicked(bool checked)
{
    checked = ui->MensenBtn->isChecked();
    if(checked){
        QFile file("E:/Crypto/ToolsCrypt/introduction/PseudoRandom.txt");
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

void PseudoRandom::on_clearBtn_clicked()
{
    ui->result->clear();
}
