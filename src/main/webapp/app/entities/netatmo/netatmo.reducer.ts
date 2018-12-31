import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { INetatmo, defaultValue } from 'app/shared/model/netatmo.model';

export const ACTION_TYPES = {
  FETCH_NETATMO_LIST: 'netatmo/FETCH_NETATMO_LIST',
  FETCH_NETATMO: 'netatmo/FETCH_NETATMO',
  CREATE_NETATMO: 'netatmo/CREATE_NETATMO',
  UPDATE_NETATMO: 'netatmo/UPDATE_NETATMO',
  DELETE_NETATMO: 'netatmo/DELETE_NETATMO',
  RESET: 'netatmo/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<INetatmo>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type NetatmoState = Readonly<typeof initialState>;

// Reducer

export default (state: NetatmoState = initialState, action): NetatmoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_NETATMO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_NETATMO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_NETATMO):
    case REQUEST(ACTION_TYPES.UPDATE_NETATMO):
    case REQUEST(ACTION_TYPES.DELETE_NETATMO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_NETATMO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_NETATMO):
    case FAILURE(ACTION_TYPES.CREATE_NETATMO):
    case FAILURE(ACTION_TYPES.UPDATE_NETATMO):
    case FAILURE(ACTION_TYPES.DELETE_NETATMO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_NETATMO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_NETATMO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_NETATMO):
    case SUCCESS(ACTION_TYPES.UPDATE_NETATMO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_NETATMO):
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

const apiUrl = 'api/netatmos';

// Actions

export const getEntities: ICrudGetAllAction<INetatmo> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_NETATMO_LIST,
  payload: axios.get<INetatmo>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<INetatmo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_NETATMO,
    payload: axios.get<INetatmo>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<INetatmo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_NETATMO,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<INetatmo> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_NETATMO,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<INetatmo> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_NETATMO,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
