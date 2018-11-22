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

import { IPin, defaultValue } from 'app/shared/model/pin.model';

export const ACTION_TYPES = {
  FETCH_PIN_LIST: 'pin/FETCH_PIN_LIST',
  FETCH_PIN: 'pin/FETCH_PIN',
  CREATE_PIN: 'pin/CREATE_PIN',
  UPDATE_PIN: 'pin/UPDATE_PIN',
  DELETE_PIN: 'pin/DELETE_PIN',
  RESET: 'pin/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPin>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PinState = Readonly<typeof initialState>;

// Reducer

export default (state: PinState = initialState, action): PinState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PIN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PIN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PIN):
    case REQUEST(ACTION_TYPES.UPDATE_PIN):
    case REQUEST(ACTION_TYPES.DELETE_PIN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PIN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PIN):
    case FAILURE(ACTION_TYPES.CREATE_PIN):
    case FAILURE(ACTION_TYPES.UPDATE_PIN):
    case FAILURE(ACTION_TYPES.DELETE_PIN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PIN_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_PIN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PIN):
    case SUCCESS(ACTION_TYPES.UPDATE_PIN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PIN):
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

const apiUrl = 'api/pins';

// Actions

export const getEntities: ICrudGetAllAction<IPin> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PIN_LIST,
    payload: axios.get<IPin>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPin> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PIN,
    payload: axios.get<IPin>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPin> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PIN,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IPin> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PIN,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPin> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PIN,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
