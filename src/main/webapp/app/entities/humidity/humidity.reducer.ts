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

import { IHumidity, defaultValue } from 'app/shared/model/humidity.model';

export const ACTION_TYPES = {
  FETCH_HUMIDITY_LIST: 'humidity/FETCH_HUMIDITY_LIST',
  FETCH_HUMIDITY: 'humidity/FETCH_HUMIDITY',
  CREATE_HUMIDITY: 'humidity/CREATE_HUMIDITY',
  UPDATE_HUMIDITY: 'humidity/UPDATE_HUMIDITY',
  DELETE_HUMIDITY: 'humidity/DELETE_HUMIDITY',
  RESET: 'humidity/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHumidity>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type HumidityState = Readonly<typeof initialState>;

// Reducer

export default (state: HumidityState = initialState, action): HumidityState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_HUMIDITY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HUMIDITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_HUMIDITY):
    case REQUEST(ACTION_TYPES.UPDATE_HUMIDITY):
    case REQUEST(ACTION_TYPES.DELETE_HUMIDITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_HUMIDITY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HUMIDITY):
    case FAILURE(ACTION_TYPES.CREATE_HUMIDITY):
    case FAILURE(ACTION_TYPES.UPDATE_HUMIDITY):
    case FAILURE(ACTION_TYPES.DELETE_HUMIDITY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_HUMIDITY_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_HUMIDITY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_HUMIDITY):
    case SUCCESS(ACTION_TYPES.UPDATE_HUMIDITY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_HUMIDITY):
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

const apiUrl = 'api/humidities';

// Actions

export const getEntities: ICrudGetAllAction<IHumidity> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HUMIDITY_LIST,
    payload: axios.get<IHumidity>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IHumidity> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HUMIDITY,
    payload: axios.get<IHumidity>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IHumidity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HUMIDITY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IHumidity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HUMIDITY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHumidity> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HUMIDITY,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
