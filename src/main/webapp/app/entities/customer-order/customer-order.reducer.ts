import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomerOrder, defaultValue } from 'app/shared/model/customer-order.model';

import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export const ACTION_TYPES = {
  SEARCH_CUSTOMERORDERS: 'customerOrder/SEARCH_CUSTOMERORDERS',
  FETCH_CUSTOMERORDER_LIST: 'customerOrder/FETCH_CUSTOMERORDER_LIST',
  FETCH_CUSTOMERORDER: 'customerOrder/FETCH_CUSTOMERORDER',
  CREATE_CUSTOMERORDER: 'customerOrder/CREATE_CUSTOMERORDER',
  UPDATE_CUSTOMERORDER: 'customerOrder/UPDATE_CUSTOMERORDER',
  DELETE_CUSTOMERORDER: 'customerOrder/DELETE_CUSTOMERORDER',
  RESET: 'customerOrder/RESET',
  CLEAR_ORDER_ITEMS: 'customerOrder/CLEAR_ORDER_ITEMS',
  ADD_ORDER_ITEM: 'customerOrder/ADD_ORDER_ITEM'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomerOrder>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CustomerOrderState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerOrderState = initialState, action): CustomerOrderState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CUSTOMERORDERS):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERORDER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERORDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMERORDER):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMERORDER):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMERORDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_CUSTOMERORDERS):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERORDER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERORDER):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMERORDER):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMERORDER):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMERORDER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CUSTOMERORDERS):
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERORDER_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERORDER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMERORDER):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMERORDER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMERORDER):
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
    case ACTION_TYPES.CLEAR_ORDER_ITEMS:
      state.entity.items.splice(0, state.entity.items.length);
      return {
        ...state
      };
    case ACTION_TYPES.ADD_ORDER_ITEM:
      state.entity.items.push({ productId: 6 }); // default product id to config
      return {
        ...state
      };
    default:
      return state;
  }
};

const apiUrl = 'api/customer-orders';
const apiSearchUrl = 'api/_search/customer-orders';

// Actions

export const getSearchEntities: ICrudSearchAction<ICustomerOrder> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_CUSTOMERORDERS,
  payload: axios.get<ICustomerOrder>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<ICustomerOrder> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERORDER_LIST,
    payload: axios.get<ICustomerOrder>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICustomerOrder> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERORDER,
    payload: axios.get<ICustomerOrder>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICustomerOrder> = entity => async dispatch => {
  // remove any empty fields rather than pass them as empty strings
  Object.keys(entity).forEach(key => entity[key] === '' && delete entity[key]);
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMERORDER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities(0, ITEMS_PER_PAGE, 'id,desc'));
  return result;
};

export const updateEntity: ICrudPutAction<ICustomerOrder> = entity => async dispatch => {
  // remove any empty fields rather than pass them as empty strings
  Object.keys(entity).forEach(key => entity[key] === '' && delete entity[key]);
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMERORDER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities(0, ITEMS_PER_PAGE, 'id,desc'));
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomerOrder> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMERORDER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities(0, ITEMS_PER_PAGE, 'id,desc'));
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const clearOrderItems = () => ({
  type: ACTION_TYPES.CLEAR_ORDER_ITEMS
});

export const addOrderItem = () => ({
  type: ACTION_TYPES.ADD_ORDER_ITEM
});
