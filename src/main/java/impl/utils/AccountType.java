package impl.utils;

public enum AccountType {
    VISITOR(0),
    NORMAL(1),
    PREMIUM(2),
    ADMIN(3),
    OWNER(4);

    private int type;

    private AccountType(final int type) {
        this.type = type;
    }

    public int getAccountType() {
        return type;
    }
}
