#include "help.h"
#include "ui_help.h"
#include <QDebug>

Help::Help(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::Help)
{
    ui->setupUi(this);
    QFile file("E:/Crypto/ToolsCrypt/README.md");
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
      qDebug() << "Can't open the file!" << endl;
    }
    QString guide;
    while (!file.atEnd()) {
      QByteArray text = file.readLine();
      guide.append(text);
      guide.append('\n');
    }
    ui->out->setReadOnly(true);
    ui->out->setPlainText(guide);

}

Help::~Help()
{
    delete ui;
}

void Help::on_OKBtn_clicked()
{
    this->close();
}

void Help::receiveHelp()
{
    this->show();
    QFile file("E:/Crypto/ToolsCrypt/README.md");
    if (!file.open(QIODevice::ReadOnly | QIODevice::Text)) {
      qDebug() << "Can't open the file!" << endl;
    }
    QString guide;
    while (!file.atEnd()) {
      QByteArray text = file.readLine();
      guide.append(text);
      guide.append('\n');
    } 
    ui->out->setReadOnly(true);
    ui->out->setPlainText(guide);
}
