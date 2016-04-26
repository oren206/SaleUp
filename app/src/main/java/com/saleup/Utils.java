package com.saleup;

class Result{
    boolean status;
    Object data;
}

interface RunMe {
    Result run(); // would be in any signature
}

class OnRunMe implements RunMe {
    public Result run(){return null;}
}

interface Callback {
    void callback(Result result); // would be in any signature
}

class OnCallback implements Callback {
    public void callback(Result result){}
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
        Result result = this.onRun.run();
        if(result.status){
            this.onSuccess.callback(result);
        }
        else{
            this.onFailed.callback(result);
        }

    }
}

public class Utils {
}
