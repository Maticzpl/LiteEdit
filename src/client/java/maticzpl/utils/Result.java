package maticzpl.utils;

// <3 rust
public class Result<R> {
    protected R value = null;

    public Result() {
        value = null;
    }
    public Result(R val) {
        value = val;
    }

    public boolean isSome() {
        return value != null;
    }

    public R unwrap() {
        assert value != null;
        return value;
    }
}
