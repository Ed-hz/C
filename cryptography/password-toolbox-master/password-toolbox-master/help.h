#ifndef HELP_H
#define HELP_H

#include <QDialog>

namespace Ui {
class Help;
}

class Help : public QDialog
{
    Q_OBJECT

public:
    explicit Help(QWidget *parent = nullptr);
    ~Help();

private slots:
    void on_OKBtn_clicked();
    void receiveHelp();

private:
    Ui::Help *ui;
};

#endif // HELP_H
