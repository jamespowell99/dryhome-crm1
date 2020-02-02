import axios from 'axios';
import { ICrudDeleteAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudSearchAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';

import { defaultValue, ICustomerOrder, ICustomerOrderReport } from 'app/shared/model/customer-order.model';
import { IOrderItem } from 'app/shared/model/order-item.model';

import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

import { ICrudSearchCustomerOrderAction } from 'app/entities/customer-order.redux-action-type';

export const ACTION_TYPES = {
  SEARCH_CUSTOMERORDERS: 'customerOrder/SEARCH_CUSTOMERORDERS',
  SEARCH_CUSTOMERORDERS_BY_INVOICE_NUM: 'customerOrder/SEARCH_CUSTOMERORDERS_BY_INVOICE_NUM',
  FETCH_CUSTOMERORDER_LIST: 'customerOrder/FETCH_CUSTOMERORDER_LIST',
  FETCH_CUSTOMERORDER: 'customerOrder/FETCH_CUSTOMERORDER',
  CREATE_CUSTOMERORDER: 'customerOrder/CREATE_CUSTOMERORDER',
  UPDATE_CUSTOMERORDER: 'customerOrder/UPDATE_CUSTOMERORDER',
  DELETE_CUSTOMERORDER: 'customerOrder/DELETE_CUSTOMERORDER',
  RESET: 'customerOrder/RESET',
  CLEAR_ORDER_ITEMS: 'customerOrder/CLEAR_ORDER_ITEMS',
  ADD_ORDER_ITEM: 'customerOrder/ADD_ORDER_ITEM',
  PAYMENT_DATE_CHANGED: 'customerOrder/PAYMENT_DATE_CHANGED',
  ITEM_QTY_CHANGED: 'customerOrder/ITEM_QTY_CHANGED',
  ITEM_PRICE_CHANGED: 'customerOrder/ITEM_PRICE_CHANGED',
  VAT_RATE_CHANGED: 'customerOrder/VAT_RATE_CHANGED',
  SEARCH_FROM_ORDER_DATE_CHANGED: 'customerOrder/SEARCH_FROM_ORDER_DATE_CHANGED',
  SEARCH_TO_ORDER_DATE_CHANGED: 'customerOrder/SEARCH_TO_ORDER_DATE_CHANGED',
  SEARCH_INVOICE_NUMBER_CHANGED: 'customerOrder/SEARCH_INVOICE_NUMBER_CHANGED'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomerOrder>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  subTotal: 0,
  vatAmount: 0,
  total: 0,
  sumSubTotals: 0,
  sumTotals: 0,

  searchFromOrderDate: '',
  searchToOrderDate: '',
  searchInvoiceNumber: ''
};

export type CustomerOrderState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerOrderState = initialState, action): CustomerOrderState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CUSTOMERORDERS):
    case REQUEST(ACTION_TYPES.SEARCH_CUSTOMERORDERS_BY_INVOICE_NUM):
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
    case FAILURE(ACTION_TYPES.SEARCH_CUSTOMERORDERS_BY_INVOICE_NUM):
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
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data.orders,
        sumSubTotals: action.payload.data.subTotal,
        sumTotals: action.payload.data.total
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CUSTOMERORDERS_BY_INVOICE_NUM):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data,
        sumSubTotals: 0,
        sumTotals: 0
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERORDER_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data,
        sumSubTotals: 0,
        sumTotals: 0
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERORDER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
        subTotal: +action.payload.data.subTotal,
        vatAmount: +action.payload.data.vatAmount,
        total: +action.payload.data.total
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
      const newItemsClearOrder = [...state.entity.items];
      newItemsClearOrder.splice(action.payload.itemToRemove, 1);
      return {
        ...state,
        entity: {
          ...state.entity,
          items: newItemsClearOrder
        },
        ...calculateTotals(state.entity.items, state.entity.vatRate)
      };
    case ACTION_TYPES.ADD_ORDER_ITEM:
      return {
        ...state,
        entity: {
          ...state.entity,
          items: [...state.entity.items, { productId: 6 }] // todo productId to config
        }
      };
    case ACTION_TYPES.PAYMENT_DATE_CHANGED:
      if (!action.payload.newPaymentDate) {
        return {
          ...state,
          entity: {
            ...state.entity,
            paymentDate: action.payload.newPaymentDate,
            paymentStatus: '',
            paymentType: '',
            paymentAmount: null
          }
        };
      } else if (!state.entity.paymentDate) {
        return {
          ...state,
          entity: {
            ...state.entity,
            paymentDate: action.payload.newPaymentDate,
            paymentStatus: 'Payment Received in Full',
            paymentAmount: +state.total.toFixed(2)
          }
        };
      }
      return {
        ...state
      };
    case ACTION_TYPES.ITEM_QTY_CHANGED:
      const newItemsQtyChanged = updateObjectInArray(state.entity.items, {
        index: action.payload.itemIdx,
        item: {
          ...state.entity.items[action.payload.itemIdx],
          quantity: action.payload.newQty
        }
      });

      return {
        ...state,
        entity: {
          ...state.entity,
          items: newItemsQtyChanged
        },
        ...calculateTotals(newItemsQtyChanged, state.entity.vatRate)
      };
    case ACTION_TYPES.ITEM_PRICE_CHANGED:
      const newItemsPriceChanged = updateObjectInArray(state.entity.items, {
        index: action.payload.itemIdx,
        item: {
          ...state.entity.items[action.payload.itemIdx],
          price: action.payload.newPrice
        }
      });
      return {
        ...state,
        entity: {
          ...state.entity,
          items: newItemsPriceChanged
        },
        ...calculateTotals(newItemsPriceChanged, state.entity.vatRate)
      };
    case ACTION_TYPES.VAT_RATE_CHANGED:
      return {
        ...state,
        ...calculateTotals(state.entity.items, action.payload.newVatRate),
        entity: {
          ...state.entity,
          vatRate: action.payload.newVatRate
        }
      };
    case ACTION_TYPES.SEARCH_FROM_ORDER_DATE_CHANGED:
      return {
        ...state,
        searchFromOrderDate: action.payload.newSearchFromOrderDate,
        searchInvoiceNumber: action.payload.newSearchFromOrderDate ? '' : state.searchInvoiceNumber
      };
    case ACTION_TYPES.SEARCH_TO_ORDER_DATE_CHANGED:
      return {
        ...state,
        searchToOrderDate: action.payload.newSearchToOrderDate,
        searchInvoiceNumber: action.payload.newSearchToOrderDate ? '' : state.searchInvoiceNumber
      };
    case ACTION_TYPES.SEARCH_INVOICE_NUMBER_CHANGED:
      return {
        ...state,
        searchInvoiceNumber: action.payload.newInvoiceNumber,
        searchFromOrderDate: '',
        searchToOrderDate: ''
      };
    default:
      return state;
  }
};

const apiUrl = 'api/customer-orders';
const apiSearchUrl = 'api/_search/customer-orders';

// Actions

export const getSearchEntities: ICrudSearchCustomerOrderAction<ICustomerOrder> = (
  searchStatus,
  searchOrderNumber,
  searchInvoiceNumber,
  searchFromOrderDate,
  searchToOrderDate,
  page,
  size,
  sort
) => {
  if (searchInvoiceNumber) {
    const requestUrl = `${apiUrl}?page=${page}&size=${size}&sort=${sort}&invoiceNumber.equals=${searchInvoiceNumber}`;
    return {
      type: ACTION_TYPES.SEARCH_CUSTOMERORDERS_BY_INVOICE_NUM,
      payload: axios.get<ICustomerOrder>(requestUrl)
    };
  } else {
    let requestUrl = `${apiUrl}/report?page=${page}&size=${size}&sort=${sort}`;

    requestUrl += `${searchStatus ? `&statuses=${searchStatus}` : ''}`;
    // requestUrl += `${searchOrderNumber ? `&orderNumber.equals=${searchOrderNumber}` : ''}`;
    // requestUrl += `${searchInvoiceNumber ? `&invoiceNumber.contains=${searchInvoiceNumber}` : ''}`;
    requestUrl += `${searchFromOrderDate ? `&startDate=${searchFromOrderDate}` : ''}`;
    requestUrl += `${searchToOrderDate ? `&endDate=${searchToOrderDate}` : ''}`;
    return {
      type: ACTION_TYPES.SEARCH_CUSTOMERORDERS,
      payload: axios.get<ICustomerOrderReport>(requestUrl)
    };
  }
};

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

export const clearOrderItems = i => ({
  type: ACTION_TYPES.CLEAR_ORDER_ITEMS,
  payload: { itemToRemove: i }
});

export const addOrderItem = () => ({
  type: ACTION_TYPES.ADD_ORDER_ITEM
});

export const paymentDateChanged = newPaymentDate => ({
  type: ACTION_TYPES.PAYMENT_DATE_CHANGED,
  payload: { newPaymentDate }
});

export const itemQtyChanged = (itemIdx, newQty) => ({
  type: ACTION_TYPES.ITEM_QTY_CHANGED,
  payload: {
    itemIdx,
    newQty
  }
});

export const itemPriceChanged = (itemIdx, newPrice) => ({
  type: ACTION_TYPES.ITEM_PRICE_CHANGED,
  payload: {
    itemIdx,
    newPrice
  }
});

export const vatRateChanged = newVatRate => ({
  type: ACTION_TYPES.VAT_RATE_CHANGED,
  payload: {
    newVatRate
  }
});

export const searchFromOrderDateChanged = newSearchFromOrderDate => ({
  type: ACTION_TYPES.SEARCH_FROM_ORDER_DATE_CHANGED,
  payload: {
    newSearchFromOrderDate
  }
});

export const searchToOrderDateChanged = newSearchToOrderDate => ({
  type: ACTION_TYPES.SEARCH_TO_ORDER_DATE_CHANGED,
  payload: {
    newSearchToOrderDate
  }
});

export const searchInvoiceNumberChanged = newInvoiceNumber => ({
  type: ACTION_TYPES.SEARCH_INVOICE_NUMBER_CHANGED,
  payload: {
    newInvoiceNumber
  }
});

const calculateTotals = (items, vatRate) => {
  const subTotal = +items.reduce((a, b) => a + (b.price || 0) * (b.quantity || 0), 0).toFixed(2);
  const vatAmount = +(subTotal * (vatRate / 100)).toFixed(2);
  const total = +(subTotal + +vatAmount).toFixed(2);
  return { subTotal, vatAmount, total };
};

const updateObjectInArray = (array, action): IOrderItem[] =>
  array.map((item, index) => {
    if (index !== action.index) {
      // This isn't the item we care about - keep it as-is
      return item;
    }

    // Otherwise, this is the one we want - return an updated value
    return {
      ...item,
      ...action.item
    };
  });
