package com.gwt.ss.client.loginable;

/**
 * Interface for asynchronous {@link com.google.gwt.user.client.rpc.RemoteService remoteService}
 * descendants have chance to be wrapped as a {@link LoginableService}.<br/>
 * example see blow:<br/> 
 * 非同步{@link com.google.gwt.user.client.rpc.RemoteService remoteService}繼承此
 * Interface時，可被包裝成{@link LoginableService}<br/>
 * 範例如下：<br/><br/>
 * 
 * &#64;RemoteServiceRelativePath(&quot;stockPrices&quot;)<br/>
 * public interface StockPriceService extends RemoteService {<br/>
 * &nbsp;&nbsp;StockPrice[] getPrices(String[] symbols);<br/>
 * }<br/><br/>
 * 
 * public interface StockPriceServiceAsync extends LoginableAsync{<br/>
 * &nbsp;&nbsp;void getPrices(String[] symbols, AsyncCallback<StockPrice[]> callback);<br/> 
 * }<br/><br/>
 * 
 * public class StockWatcher implements EntryPoint {<br/>
 * &nbsp;&nbsp;public void onModuleLoad() {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;StockPriceServiceAsync stockPriceSvc = GWT.create(StockPriceService.class);<br/> 
 *     
 * &nbsp;&nbsp;&nbsp;&nbsp;StockPriceServiceAsync stockPriceSvcProxy = GWT.create(StockPriceServiceAsync.class);<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;LoginableService<StockPriceServiceAsync> loginService = (LoginableService<StockPriceServiceAsync>) stockPriceSvcProxy;<br/><br/> 
 *     
 * &nbsp;&nbsp;&#47;*If not setRemoteService, proxy auto gerenate remote service by calling GWT.create(StockPriceServiceAsync.class)<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;若是未設定setRemoteService，proxy 會自動叫用GWT.create(StockPriceServiceAsync.class) 產生一個*&#47;<br/>
 * &nbsp;&nbsp;loginService.setRemoteService(stockPriceSvc);<br/><br/>
 *
 * &nbsp;&nbsp;&nbsp;&nbsp;&#47;*setting HasLoginHandler with a customed login dialog implements {@link HasLoginHandler}<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;設定自定的登錄畫面(必須實做{@link HasLoginHandler})*&#47;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;loginService.setHasLoginHandler(yourCustomedLoginDialog);<br/><br/>
 *     
 * &nbsp;&nbsp;&nbsp;&nbsp;&#47;*Now call RPC from proxy wrapper stockPriceSvcProxy<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;現在改用包裝後的stockPriceSvcProxy進行遠端呼叫*&#47;<br/>  
 * &nbsp;&nbsp;&nbsp;&nbsp;stockPriceSvcProxy.getPrices(symbols,new AsyncCallback<StockPrice[]>() {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#64;Override<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public void onSuccess(StockPrice[] result) {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * &#47;*<dl><dt>You must handle</dt><dd><table>
 *       <tr><td>{@link com.gwt.ss.client.exceptions.GwtAccessDeniedException GwtAccessDeniedException}:</td>
 *           <td>Whenever user has not enough authority</td> </tr>
 *       <tr><td>{@link LoginCancelException}:</td>
 *           <td>User cancel login when login dialog box display</td> </tr>  
 *       <tr><td colSpan=&quot;2&quot;>Other exception</td></tr> 
 *       </table></dd></dl>
 *       <dl><dt>您必須自行處理以下錯誤:</dt><dd><table>
 *       <tr><td>{@link com.gwt.ss.client.exceptions.GwtAccessDeniedException GwtAccessDeniedException}:</td>
 *           <td>用戶權限不足時</td> </tr>
 *       <tr><td>{@link LoginCancelException}:</td>
 *           <td>登錄畫面出現時，用戶取消登錄</td> </tr>  
 *       <tr><td colSpan=&quot;2&quot;>與其它所有錯誤</td></tr> 
 *       </table></dd></dl>*&#47;<br/>   
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#64;Override<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;public void onFailure(Throwable caught) {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;... 
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}<br/>      
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>            
 * &nbsp;&nbsp;}<br/>  
 * }<br/>        
 *       
 * @author Kent Yeh
 */
public interface LoginableAsync {
}
