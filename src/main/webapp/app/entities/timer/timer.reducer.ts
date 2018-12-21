import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITimer, defaultValue } from 'app/shared/model/timer.model';

export const ACTION_TYPES = {
  FETCH_TIMER_LIST: 'timer/FETCH_TIMER_LIST',
  FETCH_TIMER: 'timer/FETCH_TIMER',
  CREATE_TIMER: 'timer/CREATE_TIMER',
  UPDATE_TIMER: 'timer/UPDATE_TIMER',
  DELETE_TIMER: 'timer/DELETE_TIMER',
  RESET: 'timer/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITimer>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type TimerState = Readonly<typeof initialState>;

// Reducer

export default (state: TimerState = initialState, action): TimerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TIMER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TIMER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TIMER):
    case REQUEST(ACTION_TYPES.UPDATE_TIMER):
    case REQUEST(ACTION_TYPES.DELETE_TIMER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TIMER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TIMER):
    case FAILURE(ACTION_TYPES.CREATE_TIMER):
    case FAILURE(ACTION_TYPES.UPDATE_TIMER):
    case FAILURE(ACTION_TYPES.DELETE_TIMER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TIMER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TIMER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TIMER):
    case SUCCESS(ACTION_TYPES.UPDATE_TIMER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TIMER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/timers';

// Actions

export const getEntities: ICrudGetAllAction<ITimer> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TIMER_LIST,
  payload: axios.get<ITimer>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ITimer> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TIMER,
    payload: axios.get<ITimer>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITimer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TIMER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITimer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TIMER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITimer> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TIMER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
