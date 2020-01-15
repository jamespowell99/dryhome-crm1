import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IManualLabel, defaultValue } from 'app/shared/model/manual-label.model';

export const ACTION_TYPES = {
  SEARCH_MANUALLABELS: 'manualLabel/SEARCH_MANUALLABELS',
  FETCH_MANUALLABEL_LIST: 'manualLabel/FETCH_MANUALLABEL_LIST',
  FETCH_MANUALLABEL: 'manualLabel/FETCH_MANUALLABEL',
  CREATE_MANUALLABEL: 'manualLabel/CREATE_MANUALLABEL',
  UPDATE_MANUALLABEL: 'manualLabel/UPDATE_MANUALLABEL',
  DELETE_MANUALLABEL: 'manualLabel/DELETE_MANUALLABEL',
  RESET: 'manualLabel/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IManualLabel>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ManualLabelState = Readonly<typeof initialState>;

// Reducer

export default (state: ManualLabelState = initialState, action): ManualLabelState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MANUALLABELS):
    case REQUEST(ACTION_TYPES.FETCH_MANUALLABEL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MANUALLABEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MANUALLABEL):
    case REQUEST(ACTION_TYPES.UPDATE_MANUALLABEL):
    case REQUEST(ACTION_TYPES.DELETE_MANUALLABEL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_MANUALLABELS):
    case FAILURE(ACTION_TYPES.FETCH_MANUALLABEL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MANUALLABEL):
    case FAILURE(ACTION_TYPES.CREATE_MANUALLABEL):
    case FAILURE(ACTION_TYPES.UPDATE_MANUALLABEL):
    case FAILURE(ACTION_TYPES.DELETE_MANUALLABEL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MANUALLABELS):
    case SUCCESS(ACTION_TYPES.FETCH_MANUALLABEL_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MANUALLABEL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MANUALLABEL):
    case SUCCESS(ACTION_TYPES.UPDATE_MANUALLABEL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MANUALLABEL):
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

const apiUrl = 'api/manual-labels';
const apiSearchUrl = 'api/_search/manual-labels';

// Actions

export const getSearchEntities: ICrudSearchAction<IManualLabel> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MANUALLABELS,
  payload: axios.get<IManualLabel>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IManualLabel> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MANUALLABEL_LIST,
    payload: axios.get<IManualLabel>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IManualLabel> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MANUALLABEL,
    payload: axios.get<IManualLabel>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IManualLabel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MANUALLABEL,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IManualLabel> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MANUALLABEL,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IManualLabel> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MANUALLABEL,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
