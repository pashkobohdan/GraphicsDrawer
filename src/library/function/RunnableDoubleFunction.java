package library.function;

import library.function.settings.FunctionSettings;

/**
 *  Generic class for function with double argument and result type.
 */
public interface RunnableDoubleFunction {

    /**
     * Get value of function
     *
     * @param arg function argument
     * @return value from function
     */
    Double functionRun(double arg);


    /**
     * Initialize main component (if it's needed)
     *
     * @return result (OK or ERROR)
     */
    boolean initialize();



    //***********************************************************
    //                      Settings                            *
    //                                                          *
    //***********************************************************

    /**
     * Get a settings of current function (optional)
     * Maybe null
     *
     * @return
     */
    FunctionSettings getFunctionSettings();


    /**
     * For change setting of current function
     *
     * @param functionSettings
     */
    void setFunctionSettings(FunctionSettings functionSettings);


    /**
     *
     * @return
     */
    int getType();

    /**
     *
     * @param string
     * @return
     */
    RunnableDoubleFunction parseByString(String string);
}
