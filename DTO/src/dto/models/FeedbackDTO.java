package dto.models;

public class FeedbackDTO {



    private FeedbackDTO(Builder builder) {

    }

    public static final class Builder {



        public FeedbackDTO build() {
            return new FeedbackDTO(this);
        }
    }
}
