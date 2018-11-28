import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { getEntities as getControllers } from 'app/entities/controller/controller.reducer';

export const ACTION_TYPES = {
  SET: 'remote-controller/SET',
  GET: 'remote-controller/GET',
  TOGGLE: 'remote-controller/TOGGLE'
};

const initialState = {
  loading: false,
  errorMessage: null,
  success: false
};

export type RemoteControllerState = Readonly<typeof initialState>;

// Reducer

export default (state: RemoteControllerState = initialState, action): RemoteControllerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SET):
      return {
        ...state,
        errorMessage: null,
        loading: true,
        success: false
      };
    case FAILURE(ACTION_TYPES.SET):
      return {
        ...state,
        loading: false,
        success: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SET):
      return {
        ...state,
        loading: false,
        success: true
      };
    default:
      return state;
  }
};

const apiUrl = 'api/rc';

// Actions

export const set = (type, pin) => async dispatch => {
  const requestUrl = `${apiUrl}/${type}/${pin}`;
  await dispatch({
    type: ACTION_TYPES.SET,
    payload: axios.post(requestUrl)
  });
  dispatch(getControllers());
};
