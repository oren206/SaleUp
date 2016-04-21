package com.saleup;


interface RunMe {
    boolean run(); // would be in any signature
}

class OnRunMe implements RunMe {
    public boolean run(){return true;}
}

interface Callback {
    void callback(); // would be in any signature
}

class OnCallback implements Callback {
    public void callback(){}
}

class MyThread implements Runnable {

    RunMe onRun;
    Callback onSuccess;
    Callback onFailed;

    public MyThread(RunMe onRun, Callback onSuccess, Callback onFailed) {
        this.onRun = onRun;
        this.onSuccess = onSuccess;
        this.onFailed = onFailed;
    }

    public void run() {

        if(this.onRun.run()){
            this.onSuccess.callback();
        }
        else{
            this.onFailed.callback();
        }

    }
}

public class Utils {
}
