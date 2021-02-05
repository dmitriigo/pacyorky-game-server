package ee.pacyorky.gameserver.gameserver.exceptions;


//@Configuration
public class ExceptionFilter {//implements Filter {

//    private GlobalExceptionHandler globalExceptionHandler;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        try {
//
//        } catch (RuntimeException exception) {
//            ExceptionDto exceptionDto;
//
//            if (exception instanceof GlobalException) {
//                GlobalException globalException = (GlobalException) exception;
//                exceptionDto = globalExceptionHandler.handleGlobalException(globalException, request);
//            } else {
//                exceptionDto = globalExceptionHandler.handleException(exception, request);
//            }
//
//            response.setStatus(exceptionDto.getCode());
//            response.setContentType("application/json");
//
//            ObjectMapper mapper = new ObjectMapper();
//            PrintWriter out = response.getWriter();
//            out.print(mapper.writeValueAsString(exceptionDto));
//            out.flush();
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    @Override
//    public void init(FilterConfig arg0) throws ServletException {
//        ApplicationContext ctx = WebApplicationContextUtils
//                .getRequiredWebApplicationContext(arg0.getServletContext());
//        this.globalExceptionHandler = ctx.getBean(GlobalExceptionHandler.class);
//    }

}
