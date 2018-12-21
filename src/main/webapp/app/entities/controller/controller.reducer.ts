import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IController, defaultValue } from 'app/shared/model/controller.model';

export const ACTION_TYPES = {
  FETCH_CONTROLLER_LIST: 'controller/FETCH_CONTROLLER_LIST',
  FETCH_CONTROLLER: 'controller/FETCH_CONTROLLER',
  CREATE_CONTROLLER: 'controller/CREATE_CONTROLLER',
  UPDATE_CONTROLLER: 'controller/UPDATE_CONTROLLER',
  DELETE_CONTROLLER: 'controller/DELETE_CONTROLLER',
  RESET: 'controller/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IController>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ControllerState = Readonly<typeof initialState>;

// Reducer

export default (state: ControllerState = initialState, action): ControllerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CONTROLLER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONTROLLER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CONTROLLER):
    case REQUEST(ACTION_TYPES.UPDATE_CONTROLLER):
    case REQUEST(ACTION_TYPES.DELETE_CONTROLLER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CONTROLLER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONTROLLER):
    case FAILURE(ACTION_TYPES.CREATE_CONTROLLER):
    case FAILURE(ACTION_TYPES.UPDATE_CONTROLLER):
    case FAILURE(ACTION_TYPES.DELETE_CONTROLLER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTROLLER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTROLLER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONTROLLER):
    case SUCCESS(ACTION_TYPES.UPDATE_CONTROLLER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONTROLLER):
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

const apiUrl = 'api/controllers';

// Actions

export const getEntities: ICrudGetAllAction<IController> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CONTROLLER_LIST,
  payload: axios.get<IController>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IController> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONTROLLER,
    payload: axios.get<IController>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IController> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONTROLLER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IController> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONTROLLER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IController> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONTROLLER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
