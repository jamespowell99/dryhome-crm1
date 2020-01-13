import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IManualInvoice, defaultValue } from 'app/shared/model/manual-invoice.model';

export const ACTION_TYPES = {
  SEARCH_MANUALINVOICES: 'manualInvoice/SEARCH_MANUALINVOICES',
  FETCH_MANUALINVOICE_LIST: 'manualInvoice/FETCH_MANUALINVOICE_LIST',
  FETCH_MANUALINVOICE: 'manualInvoice/FETCH_MANUALINVOICE',
  CREATE_MANUALINVOICE: 'manualInvoice/CREATE_MANUALINVOICE',
  UPDATE_MANUALINVOICE: 'manualInvoice/UPDATE_MANUALINVOICE',
  DELETE_MANUALINVOICE: 'manualInvoice/DELETE_MANUALINVOICE',
  RESET: 'manualInvoice/RESET',
  CLEAR_INVOICE_ITEMS: 'customerOrder/CLEAR_INVOICE_ITEMS',
  ADD_INVOICE_ITEM: 'customerOrder/ADD_INVOICE_ITEM'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IManualInvoice>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ManualInvoiceState = Readonly<typeof initialState>;

// Reducer

export default (state: ManualInvoiceState = initialState, action): ManualInvoiceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MANUALINVOICES):
    case REQUEST(ACTION_TYPES.FETCH_MANUALINVOICE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MANUALINVOICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MANUALINVOICE):
    case REQUEST(ACTION_TYPES.UPDATE_MANUALINVOICE):
    case REQUEST(ACTION_TYPES.DELETE_MANUALINVOICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_MANUALINVOICES):
    case FAILURE(ACTION_TYPES.FETCH_MANUALINVOICE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MANUALINVOICE):
    case FAILURE(ACTION_TYPES.CREATE_MANUALINVOICE):
    case FAILURE(ACTION_TYPES.UPDATE_MANUALINVOICE):
    case FAILURE(ACTION_TYPES.DELETE_MANUALINVOICE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MANUALINVOICES):
    case SUCCESS(ACTION_TYPES.FETCH_MANUALINVOICE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MANUALINVOICE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MANUALINVOICE):
    case SUCCESS(ACTION_TYPES.UPDATE_MANUALINVOICE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MANUALINVOICE):
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
    case ACTION_TYPES.CLEAR_INVOICE_ITEMS:
      state.entity.items.splice(0, state.entity.items.length);
      return {
        ...state
      };
    case ACTION_TYPES.ADD_INVOICE_ITEM:
      state.entity.items.push({});
      return {
        ...state
      };
    default:
      return state;
  }
};

const apiUrl = 'api/manual-invoices';
const apiSearchUrl = 'api/_search/manual-invoices';

// Actions

export const getSearchEntities: ICrudSearchAction<IManualInvoice> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MANUALINVOICES,
  payload: axios.get<IManualInvoice>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IManualInvoice> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MANUALINVOICE_LIST,
    payload: axios.get<IManualInvoice>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IManualInvoice> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MANUALINVOICE,
    payload: axios.get<IManualInvoice>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IManualInvoice> = entity => async dispatch => {
  // remove any empty fields rather than pass them as empty strings
  Object.keys(entity).forEach(key => entity[key] === '' && delete entity[key]);
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MANUALINVOICE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IManualInvoice> = entity => async dispatch => {
  // remove any empty fields rather than pass them as empty strings
  Object.keys(entity).forEach(key => entity[key] === '' && delete entity[key]);
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MANUALINVOICE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IManualInvoice> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MANUALINVOICE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const clearInvoiceItems = () => ({
  type: ACTION_TYPES.CLEAR_INVOICE_ITEMS
});

export const addInvoiceItem = () => ({
  type: ACTION_TYPES.ADD_INVOICE_ITEM
});
