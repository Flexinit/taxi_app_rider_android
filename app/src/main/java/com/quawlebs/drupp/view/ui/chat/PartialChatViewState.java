package com.quawlebs.drupp.view.ui.chat;

import com.quawlebs.drupp.models.RideInfo;

interface PartialChatViewState {
    /**
     * Loading: Currently waiting for login result
     */

    final class LoadingState implements PartialChatViewState {

    }

    final class InitialState implements PartialChatViewState {

        private final RideInfo rideInfo;

        public InitialState(RideInfo response) {
            this.rideInfo = response;
        }

        public RideInfo getRideInfo() {
            return rideInfo;
        }
    }


    final class PostRatingState implements PartialChatViewState {

        String message;

        public PostRatingState(String errorResponse) {
            this.message = errorResponse;
        }

        public String getMessage() {
            return message;
        }
    }


    final class PostSendState implements PartialChatViewState {

        String message;

        public PostSendState(String errorResponse) {
            this.message = errorResponse;
        }

        public String getMessage() {
            return message;
        }
    }


    final class ErrorState implements PartialChatViewState {
        private String error;

        public ErrorState(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }


}
