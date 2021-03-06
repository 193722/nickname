package clud.zen.scholar.common.exception;

public class Exceptions {
    private static Exceptions.Clause TRUE_CLAUSE = new Exceptions.Clause(true);
    private static Exceptions.Clause FALSE_CLAUSE = new Exceptions.Clause(false);

    public Exceptions() {
    }

    public static RuntimeException newRuntimeException(String message) {
        return new AppException(message);
    }

    public static RuntimeException newRuntimeException(String message, Throwable cause) {
        return new AppException(message, cause);
    }

    public static RuntimeException newRuntimeException(Throwable cause) {
        return new AppException(cause.getMessage(), cause);
    }

    public static RuntimeException newRuntimeExceptionWithoutStackTrace(String message, Throwable cause) {
        return new AppException(message, cause, false, false);
    }

    public static RuntimeException newRuntimeExceptionWithoutStackTrace(Throwable cause) {
        return new AppException(cause.getMessage(), cause, false, false);
    }

    public static RuntimeException newRuntimeExceptionWithoutStackTrace(String message) {
        return new AppException(message, (Throwable) null, false, false);
    }

    public static RuntimeException rethrowCause(Throwable throwable) {
        Throwable cause = throwable;
        if (throwable.getCause() != null) {
            cause = throwable.getCause();
        }

        return rethrow(cause);
    }

    public static RuntimeException rethrow(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else if (throwable instanceof Error) {
            throw (Error) throwable;
        } else {
            throw new AppException(throwable.getMessage(), throwable, true, false);
        }
    }

    public static Exceptions.Clause when(boolean condition) {
        return condition ? TRUE_CLAUSE : FALSE_CLAUSE;
    }

    public static Exceptions.Clause whenNot(boolean condition) {
        return when(!condition);
    }

    public static Exceptions.Clause whenNull(Object object) {
        return when(object == null);
    }

    public static class Clause {
        private final boolean condition;

        public Clause(boolean condition) {
            this.condition = condition;
        }

        public void throwIllegalState(String message) {
            if (this.condition) {
                throw new IllegalStateException(message);
            }
        }

        public void throwIllegalArgument(String message) {
            if (this.condition) {
                throw new IllegalArgumentException(message);
            }
        }
    }
}
