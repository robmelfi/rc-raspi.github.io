import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITemperature, defaultValue } from 'app/shared/model/temperature.model';

export const ACTION_TYPES = {
  FETCH_TEMPERATURE_LIST: 'temperature/FETCH_TEMPERATURE_LIST',
  FETCH_TEMPERATURE: 'temperature/FETCH_TEMPERATURE',
  FETCH_LAST_TEMPERATURE: 'temperature/FETCH_LAST_TEMPERATURE',
  CREATE_TEMPERATURE: 'temperature/CREATE_TEMPERATURE',
  UPDATE_TEMPERATURE: 'temperature/UPDATE_TEMPERATURE',
  DELETE_TEMPERATURE: 'temperature/DELETE_TEMPERATURE',
  RESET: 'temperature/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITemperature>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  lastTemperature: NaN
};

export type TemperatureState = Readonly<typeof initialState>;

// Reducer

export default (state: TemperatureState = initialState, action): TemperatureState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TEMPERATURE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TEMPERATURE):
    case REQUEST(ACTION_TYPES.FETCH_LAST_TEMPERATURE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TEMPERATURE):
    case REQUEST(ACTION_TYPES.UPDATE_TEMPERATURE):
    case REQUEST(ACTION_TYPES.DELETE_TEMPERATURE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TEMPERATURE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TEMPERATURE):
    case FAILURE(ACTION_TYPES.FETCH_LAST_TEMPERATURE):
    case FAILURE(ACTION_TYPES.CREATE_TEMPERATURE):
    case FAILURE(ACTION_TYPES.UPDATE_TEMPERATURE):
    case FAILURE(ACTION_TYPES.DELETE_TEMPERATURE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEMPERATURE_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEMPERATURE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LAST_TEMPERATURE):
      return {
        ...state,
        loading: false,
        lastTemperature: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TEMPERATURE):
    case SUCCESS(ACTION_TYPES.UPDATE_TEMPERATURE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TEMPERATURE):
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

const apiUrl = 'api/temperatures';

// Actions

export const getEntities: ICrudGetAllAction<ITemperature> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TEMPERATURE_LIST,
    payload: axios.get<ITemperature>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ITemperature> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TEMPERATURE,
    payload: axios.get<ITemperature>(requestUrl)
  };
};

export const getLastEntity = () => {
  const requestUrl = `${apiUrl}/last`;
  return {
    type: ACTION_TYPES.FETCH_LAST_TEMPERATURE,
    payload: axios.get<ITemperature>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITemperature> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TEMPERATURE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<ITemperature> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TEMPERATURE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITemperature> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TEMPERATURE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
