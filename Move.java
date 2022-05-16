public class Move {

    Move(String positionSpecification) {
        firstMarble = positionSpecification.substring(0, 2);
        secondMarble = positionSpecification.substring(3);
    }

    private String firstMarble;
    private String secondMarble;
}
