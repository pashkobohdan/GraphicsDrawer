package library.function;

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
}
