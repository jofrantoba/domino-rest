package org.dominokit.domino.rest.shared.request;

public abstract class BaseRequest implements Request {

    public static final String REQUEST_HAVE_ALREADY_BEEN_SENT = "Request have already been sent";

    protected RequestState state;
    private boolean skipFailHandler = false;
    protected final DominoRestContext requestContext = DominoRestContext.make();

    protected final RequestState<DefaultRequestStateContext> ready = context -> startRouting();

    protected final RequestState<DefaultRequestStateContext> completed = context -> {
        throw new InvalidRequestState("This request have already been completed!. [" + this.getClass().getCanonicalName() + "]");
    };

    protected CompleteHandler completeHandler = () -> {
    };

    protected Fail fail = requestContext.getConfig().getDefaultFailHandler();

    protected final RequestState<ServerFailedRequestStateContext> failedOnServer =
            context -> {
                if (!skipFailHandler) {
                    fail.onFail(context.response);
                }
                completeHandler.onCompleted();
            };

    public BaseRequest() {
        this.state = ready;
    }

    protected void execute() {
        if (!state.equals(ready) && !state.equals(failedOnServer))
            throw new InvalidRequestState(REQUEST_HAVE_ALREADY_BEEN_SENT);
        this.state.execute(new DefaultRequestStateContext());
    }

    @Override
    public void applyState(RequestStateContext context) {
        state.execute(context);
    }

    public void skipFailHandler() {
        this.skipFailHandler = true;
    }
}
