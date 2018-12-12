import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISensor, defaultValue } from 'app/shared/model/sensor.model';

export const ACTION_TYPES = {
  FETCH_SENSOR_LIST: 'sensor/FETCH_SENSOR_LIST',
  FETCH_SENSOR: 'sensor/FETCH_SENSOR',
  CREATE_SENSOR: 'sensor/CREATE_SENSOR',
  UPDATE_SENSOR: 'sensor/UPDATE_SENSOR',
  DELETE_SENSOR: 'sensor/DELETE_SENSOR',
  SET_BLOB: 'sensor/SET_BLOB',
  RESET: 'sensor/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISensor>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SensorState = Readonly<typeof initialState>;

// Reducer

export default (state: SensorState = initialState, action): SensorState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SENSOR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SENSOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SENSOR):
    case REQUEST(ACTION_TYPES.UPDATE_SENSOR):
    case REQUEST(ACTION_TYPES.DELETE_SENSOR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SENSOR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SENSOR):
    case FAILURE(ACTION_TYPES.CREATE_SENSOR):
    case FAILURE(ACTION_TYPES.UPDATE_SENSOR):
    case FAILURE(ACTION_TYPES.DELETE_SENSOR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SENSOR_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SENSOR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SENSOR):
    case SUCCESS(ACTION_TYPES.UPDATE_SENSOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SENSOR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/sensors';

// Actions

export const getEntities: ICrudGetAllAction<ISensor> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SENSOR_LIST,
  payload: axios.get<ISensor>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISensor> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SENSOR,
    payload: axios.get<ISensor>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISensor> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SENSOR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISensor> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SENSOR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISensor> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SENSOR,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
