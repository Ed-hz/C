#include "crypto.h"
#include "pseudorandom.h"
#include "help.h"

#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    PseudoRandom p;
    Help h;
    w.show();
    h.show();

    QObject::connect(&p,SIGNAL(mainShow()),&w,SLOT(receiveMain()));
    QObject::connect(&w,SIGNAL(randomShow()),&p,SLOT(receiveRandom()));
    QObject::connect(&w,SIGNAL(helpShow()),&h,SLOT(receiveHelp()));

    return a.exec();
}
